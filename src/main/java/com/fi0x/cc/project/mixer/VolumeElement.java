package com.fi0x.cc.project.mixer;

public class VolumeElement extends AbstractMixerElement
{
    private int volume = 30;

    @Override
    public void updateElement()
    {
        //TODO: Update volume
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        volume += valueChange;
    }

    public int getVolume()
    {
        return volume;
    }

    @Override
    public String getDisplayName()
    {
        return "Volume: " + volume;
    }
}
