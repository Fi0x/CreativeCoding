package com.fi0x.cc.project.synth.synthesizers;

public class CleanGuitarSynth extends AbstractSynth
{
    public CleanGuitarSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Clean Gt."))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
