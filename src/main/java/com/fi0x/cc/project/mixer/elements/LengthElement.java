package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;
import com.fi0x.cc.project.mixer.MixerManager;

public class LengthElement extends AbstractMixerElement
{
    private int length = 2 * MixerManager.getNotesPerBeat() / 8;

    public LengthElement(MixerUIElement uiPart)
    {
        super(uiPart);

        allowedConnections.add(IncreasingElement.class);
        allowedConnections.add(ChannelElement.class);
    }

    @Override
    public void updateElement(long globalFrame, int bpm)
    {
        super.updateElement(globalFrame, bpm);
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        length += valueChange;
        if(length < 0)
            length = 0;
        if(length > 127)
            length = 127;
    }
    @Override
    public void changeSecondaryValue(int valueChange)
    {

    }
    @Override
    public void syncClock(int timerFrame)
    {
    }

    public int getLength()
    {
        int updatedLength = length;
        for(AbstractMixerElement e :connectedElements)
        {
            if(e instanceof IncreasingElement)
                updatedLength += ((IncreasingElement) e).getCurrentIncrease();
        }
        return Math.min(updatedLength, 0);
    }

    @Override
    public String getDisplayName()
    {
        return "Length: " + length;
    }
}
