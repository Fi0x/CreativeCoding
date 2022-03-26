package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;

public class ChannelElement extends AbstractMixerElement
{
    private int channel = 0;

    public ChannelElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement()
    {
        //TODO: Update channel or play sound
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        channel += valueChange;
        if(channel < 0)
            channel = 15;
        if(channel > 15)
            channel = 0;
    }

    @Override
    public String getDisplayName()
    {
        return "Channel: " + channel;
    }
}
