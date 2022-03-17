package com.fi0x.cc.project.synth.midi;

import com.fi0x.cc.logging.Logger;

import javax.sound.midi.*;
import java.util.Arrays;
import java.util.List;

public class MidiHandler
{
    public MidiHandler()
    {
        int openDevices = 0;
        MidiDevice device;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for(int i = 0; i < infos.length; i++)
        {
            try
            {
                device = MidiSystem.getMidiDevice(infos[i]);
                List<Transmitter> transmitters = device.getTransmitters();

                for(int j = 0; j < transmitters.size(); j++)
                    transmitters.get(j).setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

                Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

                device.open();
                openDevices++;
            } catch(MidiUnavailableException ignored)
            {
            }
        }
        Logger.INFO("Opened " + openDevices + " MIDI-devices");
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
            Logger.INFO("midi received: " + msg.getStatus());
        }

        public void close()
        {
        }
    }
}
