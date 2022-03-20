package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.project.synth.SynthManager;

public class EPianoSynth extends AbstractSynth
{
    public EPianoSynth(int mappedChannel)
    {
        super(mappedChannel);

        int programNumber = SynthManager.getProgramNumber("Instrument: E.Piano 1");
        if(programNumber < 0)
            throw new NullPointerException("Instrument with specified name not found");

        assert channel != null;
        channel.programChange(programNumber);
    }
}
