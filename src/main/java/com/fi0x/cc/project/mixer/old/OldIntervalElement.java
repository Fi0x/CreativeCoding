package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.mixer.MixerManager;

@Deprecated
public class OldIntervalElement
{
    private int intervalLength = 8 * MixerManager.getNotesPerBeat() / 8;
    private int currentFrame = 0;
    private boolean isActive = false;

    public void updateElement(long globalFrame, int bpm)
    {
        currentFrame++;
        if(currentFrame >= intervalLength)
        {
            currentFrame = 0;
            isActive = !isActive;
        }

        if(isActive)
        {
            //Update update receiving elements
        }
    }
    public void syncClock(int timerFrame)
    {
        currentFrame = timerFrame;
    }
}
