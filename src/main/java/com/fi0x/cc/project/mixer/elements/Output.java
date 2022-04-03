package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

import javax.sound.midi.MidiDevice;

public class Output extends AbstractElement implements IMidiConnection
{
    public Output(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    //TODO: Receive signals and output them to the correct midi device
    @Override
    public AbstractElement getConnectedOutput()
    {
        return this;
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
}
