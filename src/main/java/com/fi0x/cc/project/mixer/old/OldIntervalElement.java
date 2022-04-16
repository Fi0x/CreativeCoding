package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.OldMixerUIElement;
import com.fi0x.cc.project.mixer.MixerManager;

@Deprecated
public class OldIntervalElement extends OldAbstractMixerElement
{
    private int intervalLength = 8 * MixerManager.getNotesPerBeat() / 8;
    private int currentFrame = 0;
    private boolean isActive = false;

    public OldIntervalElement(OldMixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(OldAbstractMixerElement sender, long globalFrame, int bpm)
    {
        if(updatedFrames.contains(globalFrame))
            return;
        updatedFrames.add(globalFrame);

        currentFrame++;
        if(currentFrame >= getUpdatedInterval())
        {
            currentFrame = 0;
            isActive = !isActive;
        }

        if(isActive)
        {
            super.updateElement(sender, globalFrame, bpm);
            for(OldAbstractMixerElement e : connectedElements)
            {
                if(e == sender)
                    continue;
                e.updateElement(this, globalFrame, bpm);
                linkedUI.sendPulse(e.getLinkedUI(), 1);
            }
        }
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        intervalLength += valueChange;
        if(intervalLength < 1)
            intervalLength = 1;
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

    private int getUpdatedInterval()
    {
        int newValue = intervalLength;
        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e instanceof OldIncreasingElement)
                newValue += ((OldIncreasingElement) e).getCurrentIncrease();
        }
        return Math.max(newValue, intervalLength);
    }

    @Override
    public String getDisplayName()
    {
        return "Interval: " + getUpdatedInterval() + "(" + intervalLength + ")";
    }
}
