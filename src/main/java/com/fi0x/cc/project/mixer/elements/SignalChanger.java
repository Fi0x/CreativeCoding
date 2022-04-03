package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

public class SignalChanger extends AbstractElement implements ISignalModifier
{
    //TODO: Change channel, volume, note or length of incoming signals and output them again
    public SignalChanger(MainMixerWindow parentScreen, int x, int y)
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
}
