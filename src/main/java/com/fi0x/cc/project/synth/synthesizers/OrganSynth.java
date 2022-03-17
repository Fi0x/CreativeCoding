package com.fi0x.cc.project.synth.synthesizers;

import javax.sound.midi.*;

public class OrganSynth implements ISynthesizer
{
    private MidiChannel channel;

    public OrganSynth(int mappedChannel)
    {
        try
        {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();

            synth.loadAllInstruments (synth.getDefaultSoundbank());
            MidiChannel[] channels = synth.getChannels();
            channel = channels[Math.min(mappedChannel, channels.length - 1)];

            Instrument[] insts = synth.getLoadedInstruments();
            for (int i = 0; i < insts.length; i++)
            {
                if (insts[i].toString().startsWith("Instrument: Organ 1"))
                {
                    assert channel != null;
                    channel.programChange(i);
                    break;
                }
            }
        } catch (MidiUnavailableException ignored)
        {
        }
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
        return "Organ";
    }
}
