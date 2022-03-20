package com.fi0x.cc.project.synth.synthesizers;

public class BassSynth extends AbstractSynth
{
    public BassSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Synth Bass 1"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
