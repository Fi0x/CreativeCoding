package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;
import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.TimeCalculator;

import java.util.ArrayList;

public class DelayElement extends AbstractMixerElement
{
    private int delay = 2 * MixerManager.getNotesPerBeat() / 8;

    public DelayElement(MixerUIElement uiPart)
    {
        super(uiPart);

        allowedConnections.add(ChannelElement.class);
        allowedConnections.add(IncreasingElement.class);
        allowedConnections.add(IntervalElement.class);
        allowedConnections.add(TickElement.class);
        allowedConnections.add(TimerElement.class);
    }

    @Override
    public void updateElement(long globalFrame, int bpm)
    {
        if(updatedFrames.contains(globalFrame))
            return;
        updatedFrames.add(globalFrame);

        new Thread(() ->
        {
            try
            {
                Thread.sleep(TimeCalculator.getMillisFromBeat(delay));
            } catch(InterruptedException ignored)
            {
                return;
            }

            super.updateElement(globalFrame, bpm);
            for(AbstractMixerElement e : connectedElements)
                e.updateElement(globalFrame - 1000, bpm);
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
    public boolean canConnectTo(AbstractMixerElement otherElement)
    {
        return !(otherElement instanceof LengthElement)
                && !(otherElement instanceof NoteElement)
                && !(otherElement instanceof PitchElement)
                && !(otherElement instanceof VolumeElement);
    }

    @Override
    public String getDisplayName()
    {
        return "Delay: " + delay;
    }
}
