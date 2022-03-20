package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.synth.SynthManager;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.*;

public abstract class AbstractSynth implements ISynthesizer
{
    protected final MidiChannel channel;

    public AbstractSynth(int mappedChannel)
    {
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
    public String getInstrumentName()
    {
        return SynthManager.getInstrumentName(channel.getProgram());
    }

    @Override
    public void mute(boolean state)
    {
        channel.setMute(state);
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
}
