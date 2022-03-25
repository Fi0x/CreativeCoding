package com.fi0x.cc.project.mixer;

import java.util.ArrayList;

public abstract class AbstractMixerElement
{
    private final ArrayList<AbstractMixerElement> connectedElements = new ArrayList<>();

    public abstract void updateElement();
    public abstract void changeMainValue(int valueChange);

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
        for(AbstractMixerElement e : connectedElements)
            e.removeConnectedElement(this);
        connectedElements.clear();
    }

    public abstract String getDisplayName();
}
