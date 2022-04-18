package com.fi0x.cc.project.mixer.abstractinterfaces;

import com.fi0x.cc.project.gui.mixer.ElementUI;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.mixer.MixerSignal;
import com.fi0x.cc.project.mixer.elements.Output;

import java.util.ArrayList;

public abstract class AbstractElement extends ElementUI
{
    private Output outputNode;
    protected AbstractElement nextLink;
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

    public abstract void receiveMidi(MixerSignal msg);

    public void addInputConnection(AbstractElement incomingConnection)
    {
        inputs.add(incomingConnection);
        if(this instanceof ISignalModifier && incomingConnection instanceof INumberProvider)
            ((ISignalModifier) this).addNumberProvider((INumberProvider) incomingConnection);
    }
    public void removeAllConnections()
    {
        for(AbstractElement e : inputs)
            e.disconnectFromOutputNode();

        if(this instanceof INumberProvider && nextLink != null)
            ((ISignalModifier) nextLink).removeNumberProvider((INumberProvider) this);
    }

    public abstract void changeMainValue(int valueChange);
    public abstract String getMainValueName();
    public abstract String getMainValue();

    public Output getFinalOutput()
    {
        return outputNode;
    }

    public String getDisplayString()
    {
        return "Not \n finished";
    }
}
