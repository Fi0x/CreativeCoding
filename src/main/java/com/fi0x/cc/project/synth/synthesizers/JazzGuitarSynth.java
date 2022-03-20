package com.fi0x.cc.project.synth.synthesizers;

public class JazzGuitarSynth extends AbstractSynth
{
    public JazzGuitarSynth(int mappedChannel)
    {
        super(mappedChannel);

        for(int i = 0; i < instruments.length; i++)
        {
            if(instruments[i].toString().startsWith("Instrument: Jazz Gt."))
            {
                assert channel != null;
                channel.programChange(i);
                break;
            }
        }
    }
}
