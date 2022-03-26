package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;
import com.fi0x.cc.project.mixer.ISignalSenderElement;
import com.fi0x.cc.project.mixer.MixerManager;

public class TickElement extends AbstractMixerElement implements ISignalSenderElement
{
    private int delayBetweenTicks = 8 * MixerManager.getNotesPerBeat() / 8;
    private int currentFrame = 0;

    public TickElement(MixerUIElement uiPart)
    {
        super(uiPart);

        allowedConnections.add(ChannelElement.class);
        allowedConnections.add(DelayElement.class);
        allowedConnections.add(IncreasingElement.class);
        allowedConnections.add(IntervalElement.class);
        allowedConnections.add(TickElement.class);
        allowedConnections.add(TimerElement.class);
    }

    @Override
    public void updateElement(AbstractMixerElement sender, long globalFrame, int bpm)
    {
        if(updatedFrames.contains(globalFrame))
            return;
        updatedFrames.add(globalFrame);

        currentFrame++;
        if(currentFrame % delayBetweenTicks == 0)
        {
            super.updateElement(sender, globalFrame, bpm);
            for(AbstractMixerElement e : connectedElements)
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

    @Override
    public String getDisplayName()
    {
        return "Tick: " + delayBetweenTicks;
    }
}
