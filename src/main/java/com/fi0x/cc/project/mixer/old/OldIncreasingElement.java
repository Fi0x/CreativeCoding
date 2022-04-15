package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.OldMixerUIElement;

@Deprecated
public class OldIncreasingElement extends OldAbstractMixerElement
{
    private int valueCount = 16;
    private int increase = 0;

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

        int stepSize = 1;
        increase += stepSize;
        if(increase >= valueCount)
            increase = 0;
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        valueCount += valueChange;
    }

    public int getCurrentIncrease()
    {
        return increase;
    }

}
