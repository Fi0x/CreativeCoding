package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

public class Ticker extends AbstractElement implements ISignalCreator
{
    public Ticker(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    //TODO: Create new signals after x notes / beats
    @Override
    public boolean hasFreeInputs()
    {
        return false;
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
    public void removeAllConnections()
    {
    }
    @Override
    public void changeMainValue(int valueChange)
    {
    }
    @Override
    public void beatUpdate(long frame)
    {
    }
}
