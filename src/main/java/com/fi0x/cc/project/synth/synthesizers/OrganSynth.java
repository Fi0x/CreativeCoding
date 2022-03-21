package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.project.synth.SynthManager;

public class OrganSynth extends AbstractSynth
{
    public OrganSynth(int mappedChannel)
    {
        super(mappedChannel);

        int programNumber = SynthManager.getProgramNumber("Instrument: Organ 1", "Instrument: Drawbar Organ");
        if(programNumber < 0)
            throw new NullPointerException("Instrument with specified name not found");

        assert channel != null;
        channel.programChange(programNumber);
    }
}
