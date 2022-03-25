package com.fi0x.cc.project.mixer;

public class PitchElement extends AbstractMixerElement
{
    private int pitchDifference = 0;

    @Override
    public void updateElement()
    {
        //TODO: Update volume
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        pitchDifference += valueChange;
    }

    @Override
    public String getDisplayName()
    {
        return "Pitch: " + pitchDifference;
    }
}
