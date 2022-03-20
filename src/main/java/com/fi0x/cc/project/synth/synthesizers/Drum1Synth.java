package com.fi0x.cc.project.synth.synthesizers;

public class Drum1Synth extends AbstractSynth
{
    public Drum1Synth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Melo.Tom 1"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
