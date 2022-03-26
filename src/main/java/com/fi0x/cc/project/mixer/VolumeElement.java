package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;

public class VolumeElement extends AbstractMixerElement
{
    private int volume = 30;

    public VolumeElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long currentFrame, int bpm)
    {
        super.updateElement(currentFrame, bpm);
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
