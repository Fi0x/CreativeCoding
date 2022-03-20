package com.fi0x.cc.project.synth.synthesizers;

public class ChoirSynth extends AbstractSynth
{
    public ChoirSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Choir Aahs"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
