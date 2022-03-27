package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.MixerManager;

@Deprecated
public class OldTickElement extends OldAbstractMixerElement implements ISignalSenderElement
{
    private int delayBetweenTicks = 8 * MixerManager.getNotesPerBeat() / 8;
    private int currentFrame = 0;

    public OldTickElement(MixerUIElement uiPart)
    {
        super(uiPart);

        allowedConnections.add(OldChannelElement.class);
        allowedConnections.add(OldDelayElement.class);
        allowedConnections.add(OldIncreasingElement.class);
        allowedConnections.add(OldIntervalElement.class);
        allowedConnections.add(OldTickElement.class);
        allowedConnections.add(OldTimerElement.class);
    }

    @Override
    public void updateElement(OldAbstractMixerElement sender, long globalFrame, int bpm)
    {
        if(updatedFrames.contains(globalFrame))
            return;
        updatedFrames.add(globalFrame);

        currentFrame++;
        if(currentFrame >= getUpdatedDelay())
        {
            currentFrame = 0;
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

    private int getUpdatedDelay()
    {
        int newValue = delayBetweenTicks;
        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e instanceof OldIncreasingElement)
                newValue += ((OldIncreasingElement) e).getCurrentIncrease();
        }
        return Math.max(newValue, 1);
    }

    @Override
    public String getDisplayName()
    {
        return "Tick: " + getUpdatedDelay() + "(" + delayBetweenTicks + ")";
    }
}
