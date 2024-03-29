package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.Main;
import com.fi0x.cc.project.mixer.gui.MainMixerWindow;
import com.fi0x.cc.project.midi.MidiHandler;
import com.fi0x.cc.project.mixer.MixerSignal;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.IMidiConnection;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISignalCreator;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.MidiDevice;

public class Input extends AbstractElement implements IMidiConnection, ISignalCreator
{
    private int inputMidiDevice;

    public Input(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
        MidiHandler.inputElements.add(this);
    }
    @Override
    public void receiveMidi(MixerSignal msg)
    {
        if(nextLink == null)
            return;

        nextLink.receiveMidi(msg);

        if(Integer.toBinaryString(msg.midiMessage.getStatus()).startsWith("1000"))
            this.noteUpdate(-1000, 0.1f);
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
    public String getMainValueName()
    {
        return "Midi-Device";
    }
    @Override
    public String getMainValue()
    {
        return getConnectedMidi() == null ? "Internal" : getConnectedMidi().getDeviceInfo().toString();
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
    public void noteUpdate(long frame)
    {
        Logger.log("Input should not send beat signals and wait instead for midi-inputs from other devices", String.valueOf(Main.Template.DEBUG_WARNING));
    }

    @Override
    public String getDisplayString()
    {
        String midiInfo = getConnectedMidi() == null ? "Internal" : getConnectedMidi().getDeviceInfo().toString();
        return "Input\n" + midiInfo;
    }

    public String getConnectedMidiName()
    {
        return MidiHandler.devices.get(inputMidiDevice).getDeviceInfo().toString();
    }
    @Override
    public String getDisplayImageName()
    {
        return "images/input.png";
    }
}