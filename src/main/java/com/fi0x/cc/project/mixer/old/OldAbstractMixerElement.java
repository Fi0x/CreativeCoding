package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.OldMixerUIElement;

import java.util.ArrayList;

@Deprecated
public abstract class OldAbstractMixerElement
{

    protected final ArrayList<OldAbstractMixerElement> connectedElements = new ArrayList<>();
    protected final OldMixerUIElement linkedUI;

    protected final ArrayList<Long> updatedFrames = new ArrayList<>();

    public OldAbstractMixerElement(OldMixerUIElement uiPart)
    {
        linkedUI = uiPart;
    }

    public void updateElement(OldAbstractMixerElement sender, long globalFrame, int bpm)
    {
        if(updatedFrames.size() > 60)
            updatedFrames.remove(0);
    }
    public abstract void changeMainValue(int valueChange);

    public OldMixerUIElement getLinkedUI()
    {
        return linkedUI;
    }

}
