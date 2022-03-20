package com.fi0x.cc.project.synth.synthesizers;

public class Drum2Synth extends AbstractSynth
{
    public Drum2Synth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Trombone 2"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
