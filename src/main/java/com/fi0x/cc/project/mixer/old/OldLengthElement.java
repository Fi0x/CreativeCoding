package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.OldMixerUIElement;
import com.fi0x.cc.project.mixer.MixerManager;

@Deprecated
public class OldLengthElement extends OldAbstractMixerElement implements IParameterElement
{
    private int length = 2 * MixerManager.getNotesPerBeat() / 8;

    public OldLengthElement(OldMixerUIElement uiPart)
    {
        super(uiPart);

        allowedConnections.add(OldIncreasingElement.class);
        allowedConnections.add(OldChannelElement.class);
    }

    @Override
    public void updateElement(OldAbstractMixerElement sender, long globalFrame, int bpm)
    {
        super.updateElement(sender, globalFrame, bpm);
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
        for(OldAbstractMixerElement e :connectedElements)
        {
            if(e instanceof OldIncreasingElement)
                updatedLength += ((OldIncreasingElement) e).getCurrentIncrease();
        }
        return Math.min(Math.max(updatedLength, 0), 127);
    }

    @Override
    public String getDisplayName()
    {
        return "Length: " + getLength() + "(" + length + ")";
    }
}
