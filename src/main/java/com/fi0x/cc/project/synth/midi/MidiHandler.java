package com.fi0x.cc.project.synth.midi;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.synth.SynthManager;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.*;
import java.util.List;

public class MidiHandler
{
    public MidiHandler()
    {
        int openDevices = 0;
        MidiDevice device;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for(MidiDevice.Info info : infos)
        {
            try
            {
                device = MidiSystem.getMidiDevice(info);
                List<Transmitter> transmitters = device.getTransmitters();

                for(Transmitter transmitter : transmitters)
                    transmitter.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

                Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

                device.open();
                openDevices++;
            } catch(MidiUnavailableException ignored)
            {
            }
        }
        Logger.log("Listening on " + openDevices + " MIDI-devices", String.valueOf(LoggerManager.Template.DEBUG_INFO));
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
