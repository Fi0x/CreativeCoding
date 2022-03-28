package com.fi0x.cc.project.mixer.elements;

import java.util.ArrayList;

public abstract class AbstractElement
{
    public abstract boolean hasFreeInputs();
    public abstract ArrayList<AbstractElement> getConnectedInputs();
    public abstract AbstractElement getConnectedOutput();
    public abstract void updateFrame();
}
