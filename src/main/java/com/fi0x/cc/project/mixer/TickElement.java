package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;

public class TickElement extends AbstractMixerElement
{
    private long lastUpdatedFrame = 0;

    private int delayBetweenTicks = 60;
    private int currentFrame = 0;

    public TickElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long frameToUpdate)
    {
        if(lastUpdatedFrame == frameToUpdate)
            return;
        lastUpdatedFrame = frameToUpdate;

        currentFrame++;
        if(currentFrame % delayBetweenTicks == 0)
        {
            super.updateElement(currentFrame);
            for(AbstractMixerElement e : connectedElements)
                e.updateElement(currentFrame);
        }
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        delayBetweenTicks += valueChange;
        if(delayBetweenTicks < 1)
            delayBetweenTicks = 1;
    }

    @Override
    public String getDisplayName()
    {
        return "Tick: " + delayBetweenTicks;
    }
}
