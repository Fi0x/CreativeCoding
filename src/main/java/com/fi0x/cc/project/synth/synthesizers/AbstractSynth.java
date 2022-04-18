package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.synth.AbstractSoundVisualizer;
import com.fi0x.cc.project.gui.synth.SynthUI;
import com.fi0x.cc.project.midi.MidiSignalHelper;
import com.fi0x.cc.project.midi.MidiSignalInfo;
import com.fi0x.cc.project.synth.SynthManager;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.*;
import java.util.Objects;

public abstract class AbstractSynth implements ISynthesizer
{
    protected final MidiChannel channel;
    public final int channelNumber;
    private SynthUI linkedUI;
    private AbstractSoundVisualizer soundVisualizer;

    public AbstractSynth(int mappedChannel)
    {
        channelNumber = mappedChannel;
        channel = SynthManager.getChannel(mappedChannel);
    }

    @Override
    public void playNote(int octave, char note, int volume, int length)
    {
        channel.noteOn(MusicConverter.getNoteValue(note, octave), volume);
        try
        {
            Thread.sleep(length);
        } catch(InterruptedException ignored)
        {
        }
        channel.noteOff(note);
    }
    @Override
    public void sendMidiCommand(MidiMessage message)
    {
        MidiSignalInfo type = MidiSignalInfo.getSignalType(message.getStatus());
        byte[] data = message.getMessage();

        switch(Objects.requireNonNull(type).NAME)
        {
            case NoteOff:
                channel.noteOff(data[1], data[2]);
                linkedUI.noteTriggered(false);
                soundVisualizer.activeNotes.remove((int) data[1]);
                break;
            case NoteOn:
                channel.noteOn(data[1], data[2]);
                linkedUI.noteTriggered(true);
                soundVisualizer.activeNotes.put((int) data[1], (int) data[2]);
                soundVisualizer.lastActivity = 0;
                break;
            case PolyPressure:
                channel.setPolyPressure(data[1], data[2]);
                break;
            case ControlChange:
                channel.controlChange(data[1], data[2]);
                break;
            case ProgramChange:
                channel.programChange(data[1]);
                break;
            case ChannelPressure:
                channel.setChannelPressure(data[1]);
                break;
            case PitchBend:
                int combinedData = Integer.parseInt(MidiSignalHelper.getCombinedData(data[1], data[2]), 2);
                channel.setPitchBend(combinedData);
                soundVisualizer.pitchBendPercent = combinedData / 16383f;
                break;
        }
    }

    @Override
    public String getInstrumentName()
    {
        if(channelNumber == 9)
            return "Drumkit";
        return SynthManager.getInstrumentName(channel.getProgram()).replace("Instrument: ", "");
    }

    @Override
    public void mute(boolean state)
    {
        channel.setMute(state);
        if(state)
        {
            channel.allNotesOff();
            channel.allSoundOff();
        }
        linkedUI.setMute(state);
    }

    @Override
    public void nextInstrument()
    {
        int programNumber = channel.getProgram() + 1;
        if(programNumber > 127)
            programNumber = 0;

        channel.programChange(programNumber);
        Logger.log("Changed synth to " + SynthManager.getInstrumentName(channel.getProgram()), String.valueOf(LoggerManager.Template.DEBUG_INFO));
    }
    @Override
    public void previousInstrument()
    {
        int programNumber = channel.getProgram() - 1;
        if(programNumber < 0)
            programNumber = 127;

        channel.programChange(programNumber);
        Logger.log("Changed synth to " + SynthManager.getInstrumentName(channel.getProgram()), String.valueOf(LoggerManager.Template.DEBUG_INFO));
    }
    @Override
    public void setInstrument(String instrumentName)
    {
        int programNumber = SynthManager.getProgramNumber(instrumentName);
        if(programNumber < 0)
        {
            Logger.log("Could not load a new instrument", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
            return;
        }

        channel.programChange(programNumber);
        Logger.log("Changed synth to " + SynthManager.getInstrumentName(channel.getProgram()), String.valueOf(LoggerManager.Template.DEBUG_INFO));
    }

    @Override
    public void linkUI(SynthUI uiElement)
    {
        linkedUI = uiElement;
    }
    @Override
    public void linkVisualizer(AbstractSoundVisualizer visualizer)
    {
        soundVisualizer = visualizer;
    }
}
