package com.fi0x.cc.project.mixer;

public class TimerElement extends AbstractMixerElement
{
    private int bpm = 60;

    @Override
    public void updateElement()
    {
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        bpm += valueChange;
    }

    public int getCurrentBPM()
    {
        return bpm;
    }

    @Override
    public String getDisplayName()
    {
        return "Timer: " + bpm;
    }
}
