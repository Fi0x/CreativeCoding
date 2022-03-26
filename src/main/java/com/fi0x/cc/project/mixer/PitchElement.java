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
    public void updateElement(long currentFrame, int bpm)
    {
        super.updateElement(currentFrame, bpm);
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        pitchDifference += valueChange;
    }
    @Override
    public void syncClock(int timerFrame)
    {
    }

    public void updateChannelPitch(int channel)
    {
        //TODO: update channel pitch
    }

    @Override
    public String getDisplayName()
    {
        return "Pitch: " + pitchDifference;
    }
}
