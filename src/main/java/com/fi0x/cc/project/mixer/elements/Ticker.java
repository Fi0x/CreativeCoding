package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.TimeCalculator;
import com.fi0x.cc.project.synth.synthesizers.MusicConverter;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;

public class Ticker extends AbstractElement implements ISignalCreator, ISecondaryValues
{
    private int notesPerBeat = 1;
    private int channel;
    private int note = 60;
    private int volume = 80;
    private int noteLength = 1;

    private long lastNotePlayed = 0;

    public Ticker(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    @Override
    public void receiveMidi(ShortMessage msg)
    {
        Logger.log("Ticker received a midi message, but should not have any input connections", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        notesPerBeat += valueChange;

        if(notesPerBeat < 1)
            notesPerBeat = 1;
        else if(notesPerBeat > MixerManager.getNotesPerBeat())
            notesPerBeat = MixerManager.getNotesPerBeat();
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
    public void beatUpdate(long frame)
    {
        if(nextLink == null)
            return;
        if(frame % (MixerManager.getNotesPerBeat() / notesPerBeat) != 0)
            return;

        generateAndSendOnOffSignal();

        this.noteUpdate(-1000, 0.1f);
    }
    @Override
    public ArrayList<String> getSecondaryValueNames()
    {
        ArrayList<String> varNames = new ArrayList<>();

        varNames.add("Channel");
        varNames.add("Note");
        varNames.add("Volume");
        varNames.add("Note Length");

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
                ShortMessage msg = new ShortMessage();
                msg.setMessage(ShortMessage.NOTE_ON, currentChannel, note, volume);
                currentNext.receiveMidi(msg);
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
                return;

            try
            {
                ShortMessage msg = new ShortMessage();
                msg.setMessage(ShortMessage.NOTE_OFF, currentChannel, note, 0);
                currentNext.receiveMidi(msg);
            } catch(InvalidMidiDataException e)
            {
                Logger.log("Could not stop midi note", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
            }
        }).start();
    }
}
