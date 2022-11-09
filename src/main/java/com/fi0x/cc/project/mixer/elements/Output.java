package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.mixer.gui.MainMixerWindow;
import com.fi0x.cc.project.mixer.MixerSignal;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.IMidiConnection;
import com.fi0x.cc.project.synth.SynthManager;
import com.fi0x.cc.project.midi.MidiHandler;

import javax.sound.midi.MidiDevice;

public class Output extends AbstractElement implements IMidiConnection
{
    private int outputMidiDevice = -1;

    public Output(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);

        startColorAnimation();
    }
    @Override
    public void receiveMidi(MixerSignal msg)
    {
        if(msg.midiMessage == null)
            return;

        if(outputMidiDevice == -1)
        {
            SynthManager.handleMidiCommand(getConnectedMidiName(), msg.midiMessage);
            return;
        }

        if(getConnectedMidi() == null)
            return;

        MidiDevice device = getConnectedMidi();
        if(device != null)
        {
            device.getTransmitters().get(0).getReceiver().send(msg.midiMessage, System.currentTimeMillis());
        } else
            SynthManager.handleMidiCommand(getConnectedMidiName(), msg.midiMessage);
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        outputMidiDevice += valueChange;

        if(outputMidiDevice < -1)
            outputMidiDevice = -1;
        else if(outputMidiDevice >= MidiHandler.devices.size())
            outputMidiDevice = MidiHandler.devices.size() - 1;
    }
    @Override
    public String getMainValueName()
    {
        return "Midi-Device";
    }
    @Override
    public String getMainValue()
    {
        return getConnectedMidiName();
    }
    @Override
    public MidiDevice getConnectedMidi()
    {
        if(outputMidiDevice > -1)
            return MidiHandler.devices.get(outputMidiDevice);
        else
            return null;
    }
    @Override
    public Output getFinalOutput()
    {
        return this;
    }
    @Override
    public String getDisplayString()
    {
        return "Output\n" + getConnectedMidiName();
    }
    @Override
    public String getDisplayImageName()
    {
        return super.getDisplayImageName();//TODO: Return a useful image
    }

    private String getConnectedMidiName()
    {
        return getConnectedMidi() == null ? "Internal" : getConnectedMidi().getDeviceInfo().toString();
    }
}
