package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.synth.SynthManager;
import com.fi0x.cc.project.synth.midi.MidiHandler;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

public class Output extends AbstractElement implements IMidiConnection
{
    private int outputMidiDevice = -1;

    public Output(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);

        startColorAnimation();
    }
    @Override
    public void receiveMidi(ShortMessage msg)
    {
        if(outputMidiDevice == -1)
        {
            SynthManager.handleMidiCommand(msg);
            return;
        }

        if(getConnectedMidi() == null)
            return;

        MidiDevice device = getConnectedMidi();
        if(device != null)
        {
            try
            {
                device.getReceiver().send(msg, System.currentTimeMillis());
            } catch(MidiUnavailableException e)
            {
                System.out.println("Sending midi to '" + device.getDeviceInfo() + "' did not work");
                e.printStackTrace();
            }
        } else
            SynthManager.handleMidiCommand(msg);
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
        String midiInfo = getConnectedMidi() == null ? "Internal" : getConnectedMidi().getDeviceInfo().toString();
        return "Output\n" + midiInfo;
    }
}
