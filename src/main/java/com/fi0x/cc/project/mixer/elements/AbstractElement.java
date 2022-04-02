package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.ElementUI;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

public abstract class AbstractElement extends ElementUI
{
    public AbstractElement(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }

    public abstract boolean hasFreeInputs();
    public abstract AbstractElement getConnectedOutput();

    public abstract void updateFrame();

    public abstract void removeAllConnections();

    public abstract void changeMainValue(int valueChange);
}
