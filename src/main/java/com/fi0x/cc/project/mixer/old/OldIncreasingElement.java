package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.OldMixerUIElement;

@Deprecated
public class OldIncreasingElement extends OldAbstractMixerElement
{
    private int valueCount = 16;
    private int increase = 0;
    private int stepSize = 1;

    public OldIncreasingElement(OldMixerUIElement uiPart)
    {
        super(uiPart);
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
