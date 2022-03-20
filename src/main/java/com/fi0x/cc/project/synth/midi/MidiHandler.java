package com.fi0x.cc.project.synth.midi;

import com.fi0x.cc.exercises.Startup;
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
        Logger.getInstance().log("Opened " + openDevices + " MIDI-devices", String.valueOf(Startup.LogTemplate.INFO_GREEN));
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
            String status = Integer.toBinaryString(msg.getStatus());
            if(!status.startsWith("1001") && !status.startsWith("1000"))
                return;

            int channel = Integer.parseInt(status.substring(4), 2);
            int note = msg.getMessage()[1];
            int volume = msg.getMessage()[2];

            midiCommand(status.startsWith("1001"), channel, note, volume);
        }

        public void close()
        {
        }
    }

    private static void midiCommand(boolean turnOn, int channel, int note, int volume)
    {
        System.out.println((turnOn ? "Turn on: C=" : "Turn off: C=") + channel + ", Note=" + note + ", Volume=" + volume);
    }
}
