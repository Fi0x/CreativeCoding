package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.INumberProvider;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISignalModifier;

import javax.sound.midi.ShortMessage;

public class SignalMuter extends AbstractElement implements ISignalModifier
{
    public SignalMuter(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);

        startColorAnimation();
    }

    @Override
    public void receiveMidi(ShortMessage msg)
    {

    }
    @Override
    public void changeMainValue(int valueChange)
    {

    }
    @Override
    public String getMainValueName()
    {
        return null;
    }
    @Override
    public String getMainValue()
    {
        return null;
    }
    @Override
    public void addNumberProvider(INumberProvider provider)
    {

    }
    @Override
    public void removeNumberProvider(INumberProvider provider)
    {

    }
    //TODO: Implement functions to mute a signal when this element is active and otherwise pass it through
}
