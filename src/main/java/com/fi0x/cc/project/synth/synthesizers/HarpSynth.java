package com.fi0x.cc.project.synth.synthesizers;

public class HarpSynth extends AbstractSynth
{
    public HarpSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Harp"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
