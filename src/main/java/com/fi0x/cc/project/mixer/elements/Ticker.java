package com.fi0x.cc.project.mixer.elements;

import java.util.ArrayList;

public class Ticker extends AbstractElement implements ISignalCreator
{
    //TODO: Create new signals after x notes / beats
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
    @Override
    public void beatUpdate(long frame)
    {

    }
}
