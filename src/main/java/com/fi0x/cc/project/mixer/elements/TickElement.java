package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;
import com.fi0x.cc.project.mixer.MixerManager;

public class TickElement extends AbstractMixerElement
{
    private int delayBetweenTicks = 8 * MixerManager.getNotesPerBeat() / 8;
    private int currentFrame = 0;

    public TickElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long globalFrame, int bpm)
    {
        if(updatedFrames.contains(globalFrame))
            return;
        updatedFrames.add(globalFrame);

        currentFrame++;
        if(currentFrame % delayBetweenTicks == 0)
        {
            super.updateElement(globalFrame, bpm);
            for(AbstractMixerElement e : connectedElements)
                e.updateElement(globalFrame, bpm);
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
    public void changeSecondaryValue(int valueChange)
    {
    }
    @Override
    public void syncClock(int timerFrame)
    {
        currentFrame = timerFrame;
    }

    @Override
    public String getDisplayName()
    {
        return "Tick: " + delayBetweenTicks;
    }
}
