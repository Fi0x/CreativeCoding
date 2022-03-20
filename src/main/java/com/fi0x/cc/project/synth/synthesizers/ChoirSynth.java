package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.project.synth.SynthManager;

public class ChoirSynth extends AbstractSynth
{
    public ChoirSynth(int mappedChannel)
    {
        super(mappedChannel);

        int programNumber = SynthManager.getProgramNumber("Instrument: Choir Aahs");
        if(programNumber < 0)
            throw new NullPointerException("Instrument with specified name not found");

        assert channel != null;
        channel.programChange(programNumber);
    }
}
