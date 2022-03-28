package com.fi0x.cc.project.mixer.elements;

import javax.sound.midi.MidiDevice;
import java.util.ArrayList;

public class Input extends AbstractElement implements IMidiConnection, ISignalCreator
{
    //TODO: Receive midi signals and send them to other elements
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
