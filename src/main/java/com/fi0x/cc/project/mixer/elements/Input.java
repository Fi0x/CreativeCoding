package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.synth.midi.MidiHandler;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.ShortMessage;

public class Input extends AbstractElement implements IMidiConnection, ISignalCreator
{
    private int inputMidiDevice;

    public Input(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    @Override
    public void receiveMidi(ShortMessage msg)
    {
        if(nextLink == null)
            return;

        //TODO: Receive midi signals from external devices and send signals to next UI-element

        sendPulse(nextLink, 1);
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        inputMidiDevice += valueChange;

        if(inputMidiDevice < 0)
            inputMidiDevice = 0;
        else if(inputMidiDevice >= MidiHandler.devices.size())
            inputMidiDevice = MidiHandler.devices.size() - 1;
    }
    @Override
    public MidiDevice getConnectedMidi()
    {
        if(MidiHandler.devices.size() > inputMidiDevice)
            return MidiHandler.devices.get(inputMidiDevice);
        else
            return null;
    }
    @Override
    public void beatUpdate(long frame)
    {
        Logger.log("Input should not send beat signals and wait instead for midi-inputs from other devices", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
    }
}