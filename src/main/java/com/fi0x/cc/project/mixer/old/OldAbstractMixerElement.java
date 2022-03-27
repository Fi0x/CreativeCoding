package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

@Deprecated
public abstract class OldAbstractMixerElement
{
    protected final ArrayList<Class<? extends OldAbstractMixerElement>> allowedConnections = new ArrayList<>();

    protected final ArrayList<OldAbstractMixerElement> connectedElements = new ArrayList<>();
    protected final MixerUIElement linkedUI;

    protected final ArrayList<Long> updatedFrames = new ArrayList<>();

    public OldAbstractMixerElement(MixerUIElement uiPart)
    {
        linkedUI = uiPart;
    }

    public void updateElement(OldAbstractMixerElement sender, long globalFrame, int bpm)
    {
        linkedUI.blinkStroke(0.03f * bpm / 60);
        if(updatedFrames.size() > 60)
            updatedFrames.remove(0);
    }
    public abstract void changeMainValue(int valueChange);
    public abstract void changeSecondaryValue(int valueChange);
    public abstract void syncClock(int timerFrame);

    public boolean canConnectTo(OldAbstractMixerElement otherElement)
    {
        return allowedConnections.contains(otherElement.getClass());
    }
    public void addConnectedElement(OldAbstractMixerElement newConnection)
    {
        if(!connectedElements.contains(newConnection))
            connectedElements.add(newConnection);
    }
    public ArrayList<MixerUIElement> getConnectedUIs()
    {
        ArrayList<MixerUIElement> uis = new ArrayList<>();
        for(OldAbstractMixerElement e : connectedElements)
            uis.add(e.linkedUI);
        return uis;
    }
    public MixerUIElement getLinkedUI()
    {
        return linkedUI;
    }
    public void removeConnectedElement(OldAbstractMixerElement connectionToRemove)
    {
        connectedElements.remove(connectionToRemove);
    }
    public void removeAllConnections()
    {
        try
        {
            for(OldAbstractMixerElement e : connectedElements)
                e.removeConnectedElement(this);
            connectedElements.clear();
        } catch(ConcurrentModificationException ignored)
        {
        }
    }
    public void replaceConnections(OldAbstractMixerElement newElement)
    {
        for(OldAbstractMixerElement e : connectedElements)
        {
            e.removeConnectedElement(this);
            e.addConnectedElement(newElement);
            newElement.addConnectedElement(e);
        }
    }

    public abstract String getDisplayName();
}
