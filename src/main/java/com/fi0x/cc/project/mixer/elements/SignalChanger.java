package com.fi0x.cc.project.mixer.elements;

import java.util.ArrayList;

public class SignalChanger extends AbstractElement implements ISignalModifier
{
    //TODO: Change channel, volume, note or length of incoming signals and output them
    @Override
    public boolean hasFreeInputs()
    {
        return false;
    }
    @Override
    public ArrayList<AbstractElement> getConnectedInputs()
    {
        return null;
    }
    @Override
    public AbstractElement getConnectedOutput()
    {
        return null;
    }
    @Override
    public void updateFrame()
    {

    }
}
