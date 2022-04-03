package com.fi0x.cc.project.synth.midi;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.synth.SynthManager;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class MidiHandler
{
    public final static ArrayList<MidiDevice> devices = new ArrayList<>();

    public MidiHandler()
    {
        Logger.log("Loading MIDI-Devices", String.valueOf(LoggerManager.Template.DEBUG_INFO));
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for(MidiDevice.Info info : infos)
        {
            try
            {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                List<Transmitter> transmitters = device.getTransmitters();

                for(Transmitter transmitter : transmitters)
                    transmitter.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

                Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

                device.open();
                devices.add(device);
                Logger.log("\tFound device: " + info, String.valueOf(LoggerManager.Template.DEBUG_INFO));
            } catch(MidiUnavailableException e)
            {
                Logger.log("\tCould not load device: " + info, String.valueOf(LoggerManager.Template.DEBUG_WARNING));
            }
        }
    }

    public static class MidiInputReceiver implements Receiver
    {
        public String name;
        public MidiInputReceiver(String name)
        {
            this.name = name;
        }

        public void send(MidiMessage msg, long timeStamp)
        {
            SynthManager.handleMidiCommand(msg);
        }

        public void close()
        {
        }
    }
}
