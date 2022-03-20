package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.Startup;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.*;

public abstract class AbstractSynth implements ISynthesizer
{
    protected MidiChannel channel;
    Instrument[] instruments;

    public AbstractSynth(int mappedChannel)
    {
        try
        {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();

            synth.loadAllInstruments(synth.getDefaultSoundbank());
            MidiChannel[] channels = synth.getChannels();
            channel = channels[Math.min(mappedChannel, channels.length - 1)];
            instruments = synth.getLoadedInstruments();
        } catch(MidiUnavailableException ignored)
        {
            Logger.getInstance().log("Could not assign channel " + mappedChannel + " for " + getInstrumentName(), String.valueOf(Startup.LogTemplate.INFO_YELLOW));
        }

    }
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
    public String getInstrumentName()
    {
        return instruments[channel.getProgram()].toString().split("bank")[0];
    }

    @Override
    public void mute(boolean state)
    {
        channel.setMute(state);
    }
}
