package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;

@Deprecated
public class OldIncreasingElement extends OldAbstractMixerElement implements INumberElement
{
    private int valueCount = 16;
    private int increase = 0;
    private int stepSize = 1;

    public OldIncreasingElement(MixerUIElement uiPart)
    {
        super(uiPart);

        allowedConnections.add(OldChannelElement.class);
        allowedConnections.add(OldLengthElement.class);
        allowedConnections.add(OldNoteElement.class);
        allowedConnections.add(OldPitchElement.class);
        allowedConnections.add(OldVolumeElement.class);
        allowedConnections.add(OldDelayElement.class);
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

        super.updateElement(sender, globalFrame, bpm);

        increase += stepSize;
        if(increase >= valueCount)
            increase = 0;
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        valueCount += valueChange;
    }
    @Override
    public void changeSecondaryValue(int valueChange)
    {
        stepSize += valueChange;
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
        return "Iterate: " + valueCount + "\nStep: " + stepSize;
    }
}
