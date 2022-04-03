package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

import javax.sound.midi.MidiDevice;

public class Output extends AbstractElement implements IMidiConnection
{
    public Output(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    //TODO: Receive signals and output them to a selected midi device
    @Override
    public void updateFrame()
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
    public Output getFinalOutput()
    {
        return this;
    }
}
