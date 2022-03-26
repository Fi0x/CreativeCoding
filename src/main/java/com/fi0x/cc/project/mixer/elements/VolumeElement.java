package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;

public class VolumeElement extends AbstractMixerElement
{
    private int volume = 30;

    public VolumeElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long globalFrame, int bpm)
    {
        super.updateElement(globalFrame, bpm);
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
    @Override
    public void changeSecondaryValue(int valueChange)
    {
    }
    @Override
    public void syncClock(int timerFrame)
    {
    }
    @Override
    public boolean canConnectTo(AbstractMixerElement otherElement)
    {
        return otherElement instanceof ChannelElement;
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
