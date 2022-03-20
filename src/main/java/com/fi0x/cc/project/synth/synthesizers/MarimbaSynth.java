package com.fi0x.cc.project.synth.synthesizers;

public class MarimbaSynth extends AbstractSynth
{
    public MarimbaSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Marimba"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
