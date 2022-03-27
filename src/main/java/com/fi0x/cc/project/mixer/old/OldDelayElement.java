package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.TimeCalculator;

@Deprecated
public class OldDelayElement extends OldAbstractMixerElement implements ISignalSenderElement
{
    private int delay = 2 * MixerManager.getNotesPerBeat() / 8;

    public OldDelayElement(MixerUIElement uiPart)
    {
        super(uiPart);

        allowedConnections.add(OldChannelElement.class);
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

        new Thread(() ->
        {
            try
            {
                Thread.sleep(TimeCalculator.getMillisFromBeat(getUpdatedDelay()));
            } catch(InterruptedException ignored)
            {
                return;
            }

            super.updateElement(sender, globalFrame, bpm);
            for(OldAbstractMixerElement e : connectedElements)
            {
                if(e == sender)
                    continue;
                e.updateElement(this, globalFrame - 1000, bpm);
                linkedUI.sendPulse(e.getLinkedUI(), 1);
            }
        }).start();
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        delay += valueChange;
        if(delay < 0)
            delay = 0;
    }
    @Override
    public void changeSecondaryValue(int valueChange)
    {
    }
    @Override
    public void syncClock(int timerFrame)
    {
    }
    @Override
    public boolean canConnectTo(OldAbstractMixerElement otherElement)
    {
        return !(otherElement instanceof OldLengthElement)
                && !(otherElement instanceof OldNoteElement)
                && !(otherElement instanceof OldPitchElement)
                && !(otherElement instanceof OldVolumeElement);
    }

    private int getUpdatedDelay()
    {
        int newDelay = delay;
        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e instanceof OldIncreasingElement)
                newDelay += ((OldIncreasingElement) e).getCurrentIncrease();
        }
        return Math.max(newDelay, delay);
    }

    @Override
    public String getDisplayName()
    {
        return "Delay: " + getUpdatedDelay() + "(" + delay + ")";
    }
}
