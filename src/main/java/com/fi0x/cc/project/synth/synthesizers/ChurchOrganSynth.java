package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.project.synth.SynthManager;

public class ChurchOrganSynth extends AbstractSynth
{
    public ChurchOrganSynth(int mappedChannel)
    {
        super(mappedChannel);

        int programNumber = SynthManager.getProgramNumber("Instrument: Church Org");
        if(programNumber < 0)
            throw new NullPointerException("Instrument with specified name not found");

        assert channel != null;
        channel.programChange(programNumber);
    }
}
