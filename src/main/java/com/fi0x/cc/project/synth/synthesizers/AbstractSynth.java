package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.synth.AbstractSoundVisualizer;
import com.fi0x.cc.project.gui.synth.SynthUI;
import com.fi0x.cc.project.synth.SynthManager;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.*;

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
        String status = Integer.toBinaryString(message.getStatus());
        byte[] data = message.getMessage();

        switch(status.substring(0, 4))
        {
            case "1000":
                channel.noteOff(data[1], data[2]);
                linkedUI.noteTriggered(false);
                soundVisualizer.activeNotes.remove((int) data[1]);
                break;
            case "1001":
                channel.noteOn(data[1], data[2]);
                linkedUI.noteTriggered(true);
                soundVisualizer.activeNotes.put((int) data[1], (int) data[2]);
                soundVisualizer.lastActivity = 0;
                break;
            case "1010":
                channel.setPolyPressure(data[1], data[2]);
                break;
            case "1011":
                channel.controlChange(data[1], data[2]);
                break;
            case "1100":
                channel.programChange(data[1]);
                break;
            case "1101":
                channel.setChannelPressure(data[1]);
                break;
            case "1110":
                String binary = Integer.toBinaryString(data[1]) + Integer.toBinaryString(data[2]);
                channel.setPitchBend(Integer.parseInt(binary, 2));
                soundVisualizer.pitchBendPercent = ((float) Integer.parseInt(binary, 2)) / 16383;
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
