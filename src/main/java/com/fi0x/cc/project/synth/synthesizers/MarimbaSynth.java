package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.project.synth.SynthManager;

public class MarimbaSynth extends AbstractSynth
{
    public MarimbaSynth(int mappedChannel)
    {
        super(mappedChannel);

        int programNumber = SynthManager.getProgramNumber("Instrument: Marimba");
        if(programNumber < 0)
            throw new NullPointerException("Instrument with specified name not found");

        assert channel != null;
        channel.programChange(programNumber);
    }
}
