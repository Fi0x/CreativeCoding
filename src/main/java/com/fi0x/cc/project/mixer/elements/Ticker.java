package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

public class Ticker extends AbstractElement implements ISignalCreator
{
    //TODO: Create pulses after x notes / beats that can be modified further
    public Ticker(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    @Override
    public void updateFrame()
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
