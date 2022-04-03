package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.ElementUI;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

import java.util.ArrayList;

public abstract class AbstractElement extends ElementUI
{
    private Output outputNode;
    private AbstractElement nextLink;
    private final ArrayList<AbstractElement> inputs = new ArrayList<>();

    public AbstractElement(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }

    public AbstractElement getConnectedNode()
    {
        return nextLink;
    }
    public void connectToNode(AbstractElement nextNode)
    {
        nextLink = nextNode;
        outputNode = nextNode.getFinalOutput();

        nextNode.addInputConnection(this);
    }
    public void disconnectFromOutputNode()
    {
        nextLink = null;
        outputNode = null;
    }

    public abstract void updateFrame();

    public void addInputConnection(AbstractElement incomingConnection)
    {
        inputs.add(incomingConnection);
    }
    public void removeAllConnections()
    {
        for(AbstractElement e : inputs)
            e.disconnectFromOutputNode();
    }

    public abstract void changeMainValue(int valueChange);

    public Output getFinalOutput()
    {
        return outputNode;
    }
}
