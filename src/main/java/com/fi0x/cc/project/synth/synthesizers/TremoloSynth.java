package com.fi0x.cc.project.synth.synthesizers;

public class TremoloSynth extends AbstractSynth
{
    public TremoloSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Tremolo Str"))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
