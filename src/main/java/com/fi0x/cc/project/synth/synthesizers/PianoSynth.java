package com.fi0x.cc.project.synth.synthesizers;

public class PianoSynth extends AbstractSynth
{
    public PianoSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Piano 1"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
