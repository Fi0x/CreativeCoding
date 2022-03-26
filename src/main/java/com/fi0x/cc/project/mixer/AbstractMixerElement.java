package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public abstract class AbstractMixerElement
{
    protected final ArrayList<AbstractMixerElement> connectedElements = new ArrayList<>();
    protected final MixerUIElement linkedUI;

    protected final ArrayList<Long> updatedFrames = new ArrayList<>();

    public AbstractMixerElement(MixerUIElement uiPart)
    {
        linkedUI = uiPart;
    }

    public void updateElement(long globalFrame, int bpm)
    {
        linkedUI.blinkStroke(0.03f * bpm / 60);
        if(updatedFrames.size() > 60)
            updatedFrames.remove(0);
    }
    public abstract void changeMainValue(int valueChange);
    public abstract void changeSecondaryValue(int valueChange);
    public abstract void syncClock(int timerFrame);

    public void addConnectedElement(AbstractMixerElement newConnection)
    {
        if(!connectedElements.contains(newConnection))
            connectedElements.add(newConnection);
    }
    public void removeConnectedElement(AbstractMixerElement connectionToRemove)
    {
        connectedElements.remove(connectionToRemove);
    }
    public void removeAllConnections()
    {
        try
        {
            for(AbstractMixerElement e : connectedElements)
                e.removeConnectedElement(this);
            connectedElements.clear();
        } catch(ConcurrentModificationException ignored)
        {
        }
    }
    public void replaceConnections(AbstractMixerElement newElement)
    {
        for(AbstractMixerElement e : connectedElements)
        {
            e.removeConnectedElement(this);
            e.addConnectedElement(newElement);
            newElement.addConnectedElement(e);
        }
    }

    public abstract String getDisplayName();
}
