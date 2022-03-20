package com.fi0x.cc.project.synth.synthesizers;

public class VibraphoneSynth extends AbstractSynth
{
    public VibraphoneSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Vibraphone"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
