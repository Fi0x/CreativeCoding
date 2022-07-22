package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.Main;
import com.fi0x.cc.project.mixer.gui.MainMixerWindow;
import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.MixerSignal;
import com.fi0x.cc.project.mixer.TimeCalculator;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISecondaryValues;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISignalCreator;
import com.fi0x.cc.project.synth.synthesizers.MusicConverter;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.UUID;

public class Ticker extends AbstractElement implements ISignalCreator, ISecondaryValues
{
    private int notesPerBeat = 1;
    private int channel;
    private int note = 60;
    private int volume = 80;
    private int noteLength = 1;
    private int noteShift = 0;

    private long lastNotePlayed = 0;

    public Ticker(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    @Override
    public void receiveMidi(MixerSignal msg)
    {
        Logger.log("Ticker received a midi message, but should not have any input connections", String.valueOf(Main.Template.DEBUG_WARNING));
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        int notesPerBeatPower = (int) (Math.log(MixerManager.getNotesPerBeat()) / Math.log(2));
        int currentPower = (int) (Math.log(notesPerBeat) / Math.log(2));

        currentPower += valueChange;

        if(currentPower < 0)
            currentPower = 0;
        else if(currentPower > notesPerBeatPower)
            currentPower = notesPerBeatPower;

        notesPerBeat = (int) Math.pow(2, currentPower);
    }
    @Override
    public String getMainValueName()
    {
        return "Notes/Beat";
    }
    @Override
    public String getMainValue()
    {
        return String.valueOf(notesPerBeat);
    }
    @Override
    public void noteUpdate(long frame)
    {
        if(nextLink == null)
            return;
        if(frame % (MixerManager.getNotesPerBeat() / notesPerBeat) != 0)
            return;

        generateAndSendOnOffSignal();
    }
    @Override
    public ArrayList<String> getSecondaryValueNames()
    {
        ArrayList<String> varNames = new ArrayList<>();

        varNames.add("Channel");
        varNames.add("Note");
        varNames.add("Volume");
        varNames.add("Note Length");
        varNames.add("Note Shift");

        return varNames;
    }
    @Override
    public void updateSecondaryValue(String valueName, int valueChange)
    {
        switch (valueName)
        {
            case "Channel":
                channel += valueChange;
                if(channel < 0)
                    channel = 0;
                else if(channel > 15)
                    channel = 15;
                break;
            case "Note":
                note += valueChange;
                if(note < 0)
                    note = 0;
                else if(note > 127)
                    note = 127;
                break;
            case "Volume":
                volume += valueChange;
                if(volume < 0)
                    volume = 0;
                else if(volume > 127)
                    volume = 127;
                break;
            case "Note Length":
                noteLength += valueChange;
                if(noteLength < 0)
                    noteLength = 0;
                break;
            case "Note Shift":
                noteShift += valueChange;
                if(noteShift < 0)
                    noteShift = 0;
                else if(noteShift >= MixerManager.getNotesPerBeat())
                    noteShift = MixerManager.getNotesPerBeat() - 1;
                break;
        }
    }
    @Override
    public String getSecondaryValue(String valueName)
    {
        switch (valueName)
        {
            case "Channel":
                return String.valueOf(channel);
            case "Note":
                return "" + MusicConverter.getOctave(note) + MusicConverter.getNoteName(note);
            case "Volume":
                return String.valueOf(volume);
            case "Note Length":
                return String.valueOf(noteLength);
            case "Note Shift":
                return String.valueOf(noteShift);
        }
        return "";
    }
    @Override
    public String getDisplayString()
    {
        return "Ticker\n" + notesPerBeat + (notesPerBeat > 1 ? "Notes" : "Note") + "/Beat";
    }

    private void generateAndSendOnOffSignal()
    {
        lastNotePlayed = System.currentTimeMillis();

        new Thread(() ->
        {
            AbstractElement currentNext = nextLink;
            int currentChannel = channel;

            try
            {
                Thread.sleep(TimeCalculator.getMillisFromBeat(noteShift));
            } catch(InterruptedException ignored)
            {
            }

            this.noteUpdate(-1000, 0.1f);

            UUID multiSignalID = UUID.randomUUID();
            try
            {
                ShortMessage msg = new ShortMessage();
                msg.setMessage(ShortMessage.NOTE_ON, currentChannel, note, volume);
                MixerSignal signal = new MixerSignal();
                signal.id = multiSignalID;
                signal.hasMore = true;
                signal.midiMessage = msg;
                currentNext.receiveMidi(signal);
            } catch(InvalidMidiDataException e)
            {
                return;
            }

            int delay = TimeCalculator.getMillisFromBeat(noteLength);
            try
            {
                Thread.sleep(delay);
            } catch(InterruptedException ignored)
            {
            }

            if(lastNotePlayed > System.currentTimeMillis() - delay && currentChannel == channel)
            {
                MixerSignal signal = new MixerSignal();
                signal.id = multiSignalID;
                currentNext.receiveMidi(signal);
                return;
            }

            try
            {
                ShortMessage msg = new ShortMessage();
                msg.setMessage(ShortMessage.NOTE_OFF, currentChannel, note, 0);
                MixerSignal signal = new MixerSignal();
                signal.id = multiSignalID;
                signal.midiMessage = msg;
                currentNext.receiveMidi(signal);
            } catch(InvalidMidiDataException e)
            {
                Logger.log("Could not stop midi note", String.valueOf(Main.Template.DEBUG_WARNING));
            }
        }).start();
    }
}
