package com.fi0x.cc.project.mixer;

public class OscillatorElement extends AbstractMixerElement
{
    private int ticksPerSecond = 1;

    @Override
    public void updateElement()
    {
        //TODO: Update volume
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        ticksPerSecond += valueChange;
    }

    @Override
    public String getDisplayName()
    {
        return "Oscillator: " + ticksPerSecond;
    }
}
