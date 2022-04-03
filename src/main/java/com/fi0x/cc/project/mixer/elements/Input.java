package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

import javax.sound.midi.MidiDevice;

public class Input extends AbstractElement implements IMidiConnection, ISignalCreator
{
    //TODO: Create pulses / signals from midi-input devices that can be modified further
    public Input(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    //TODO: Receive midi signals from external devices and send signals to other UI-elements
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
    public void beatUpdate(long frame)
    {
    }
}