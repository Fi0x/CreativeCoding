package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;

public class TimerElement extends AbstractMixerElement
{
    private int bpm = 60;
    private int currentFrame = 0;

    public TimerElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement()
    {
        currentFrame++;

        if(currentFrame % 60 == 0)
        {
            currentFrame = 0;
            linkedUI.blinkColor();
        }

        for(AbstractMixerElement e : connectedElements)
            e.updateElement();
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        bpm += valueChange;
        if(bpm < 1)
            bpm = 1;
        if(bpm > 1000)
            bpm = 1000;
    }

    public int getCurrentBPM()
    {
        return bpm;
    }

    @Override
    public String getDisplayName()
    {
        return "BPM: " + bpm;
    }
}
