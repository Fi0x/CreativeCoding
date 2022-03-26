package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;

public class OscillatorElement extends AbstractMixerElement
{
    private long lastUpdatedFrame = 0;
    private int ticksPerSecond = 1;

    public OscillatorElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long currentFrame, int bpm)
    {
        if(lastUpdatedFrame == currentFrame)
            return;
        lastUpdatedFrame = currentFrame;

        super.updateElement(currentFrame, bpm);
        //TODO: Update tickrate
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        ticksPerSecond += valueChange;
        if(ticksPerSecond < 1)
            ticksPerSecond = 1;
    }

    @Override
    public String getDisplayName()
    {
        return "Oscillator: " + ticksPerSecond;
    }
}
