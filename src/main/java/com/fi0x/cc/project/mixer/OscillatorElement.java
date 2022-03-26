package com.fi0x.cc.project.mixer;

public class OscillatorElement extends AbstractMixerElement
{
    private int ticksPerSecond = 1;

    @Override
    public void updateElement()
    {
        //TODO: Update tickrate
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        ticksPerSecond += valueChange;
        if(ticksPerSecond < 1)
            ticksPerSecond = 1;
    }

    @Override
    public String getDisplayName()
    {
        return "Oscillator: " + ticksPerSecond;
    }
}
