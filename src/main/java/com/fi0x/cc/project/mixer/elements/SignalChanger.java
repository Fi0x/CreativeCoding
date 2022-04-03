package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

public class SignalChanger extends AbstractElement implements ISignalModifier
{
    public SignalChanger(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    //TODO: Change channel, volume, note or length of incoming signals and output them
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
}
