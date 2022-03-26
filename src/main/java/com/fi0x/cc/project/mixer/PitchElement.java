package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;

public class PitchElement extends AbstractMixerElement
{
    private int pitchDifference = 0;

    public PitchElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long currentFrame)
    {
        super.updateElement(currentFrame);
        //TODO: Update pitch
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
