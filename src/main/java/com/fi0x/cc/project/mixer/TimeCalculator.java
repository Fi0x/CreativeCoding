package com.fi0x.cc.project.mixer;

public class TimeCalculator
{
    public static int getMillisFromBeat(int noteCount)
    {
        float beat = 60000f / MixerManager.getBPM();
        float beatParts = beat / MixerManager.getNotesPerBeat();
        return (int) (beatParts * noteCount);
    }
}
