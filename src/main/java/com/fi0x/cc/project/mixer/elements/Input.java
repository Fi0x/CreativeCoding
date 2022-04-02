package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

import javax.sound.midi.MidiDevice;

public class Input extends AbstractElement implements IMidiConnection, ISignalCreator
{
    public Input(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    //TODO: Receive midi signals and send them to other elements
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
    public boolean isOutput()
    {
        return false;
    }
    @Override
    public boolean isInput()
    {
        return false;
    }
    @Override
    public MidiDevice getConnectedMidi()
    {
        return null;
    }
    @Override
    public void beatUpdate(long frame)
    {
    }
}