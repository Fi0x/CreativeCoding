package com.fi0x.cc.project.mixer.old;

@Deprecated
public class OldIncreasingElement
{
    private int valueCount = 16;
    private int increase = 0;
    private int stepSize = 1;

    public void updateElement(long globalFrame, int bpm)
    {
        increase += stepSize;
        if(increase >= valueCount)
            increase = 0;
    }

    public int getCurrentIncrease()
    {
        return increase;
    }
}
