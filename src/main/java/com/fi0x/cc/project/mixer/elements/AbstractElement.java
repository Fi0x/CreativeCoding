package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.ElementUI;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

public abstract class AbstractElement extends ElementUI
{
    private Output outputNode;
    private AbstractElement outputLink;

    public AbstractElement(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }

    public AbstractElement getConnectedOutput()
    {
        return outputLink;
    }
    public void connectToNode(AbstractElement nextNode)
    {
        outputLink = nextNode;
        outputNode = nextNode.getFinalOutput();
    }

    public abstract void updateFrame();

    public abstract void removeAllConnections();

    public abstract void changeMainValue(int valueChange);

    public Output getFinalOutput()
    {
        return outputNode;
    }
}
