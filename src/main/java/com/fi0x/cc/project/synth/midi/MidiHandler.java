package com.fi0x.cc.project.synth.midi;

import com.fi0x.cc.Startup;
import io.fi0x.javalogger.LogSettings;

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
        LogSettings.getLOGFromTemplate("Opened " + openDevices + " MIDI-devices", String.valueOf(Startup.LogTemplate.INFO_GREEN));
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
            int channel = msg.getMessage()[0];
            int note = msg.getMessage()[1];
            int volume = msg.getMessage()[2];
            LogSettings.getLOGFromTemplate("midi received: " + msg.getStatus(), String.valueOf(Startup.LogTemplate.DEBUG_INFO));
        }

        public void close()
        {
        }
    }
}
