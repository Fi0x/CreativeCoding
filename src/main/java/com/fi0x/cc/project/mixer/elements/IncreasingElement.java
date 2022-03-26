package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;

public class IncreasingElement extends AbstractMixerElement
{
    private int valueCount = 16;
    private int increase = 0;

    public IncreasingElement(MixerUIElement uiPart)
    {
        super(uiPart);

        allowedConnections.add(ChannelElement.class);
        allowedConnections.add(LengthElement.class);
        allowedConnections.add(NoteElement.class);
        allowedConnections.add(PitchElement.class);
        allowedConnections.add(VolumeElement.class);
        allowedConnections.add(DelayElement.class);
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

        super.updateElement(globalFrame, bpm);

        increase++;
        if(increase >= valueCount)
            increase = 0;
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        valueCount += valueChange;
        if(valueCount < 0)
            valueCount = 0;
    }
    @Override
    public void changeSecondaryValue(int valueChange)
    {
    }
    @Override
    public void syncClock(int timerFrame)
    {
    }

    public int getCurrentIncrease()
    {
        return increase;
    }

    @Override
    public String getDisplayName()
    {
        return "Iterate: " + valueCount;
    }
}
