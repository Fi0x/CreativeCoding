package com.fi0x.cc.project.synth.synthesizers;

public class ViolinSynth extends AbstractSynth
{
    public ViolinSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Violin"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
