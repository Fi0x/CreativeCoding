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
        if(volume < 0)
            volume = 0;
        if(volume > 127)
            volume = 127;
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
