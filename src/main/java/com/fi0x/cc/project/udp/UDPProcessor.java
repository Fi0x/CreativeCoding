package com.fi0x.cc.project.udp;

import com.fi0x.cc.Main;
import com.fi0x.cc.project.synth.SynthManager;
import io.fi0x.javalogger.logging.Logger;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UDPProcessor
{
    private static UDPProcessor instance = null;

    byte[] receiveData;
    byte[] sendData;
    UDPListener udpListener;

    private UDPProcessor()
    {
        receiveData = new byte[UDPDefinitions.BUFFER_SIZE];
        sendData = new byte[UDPDefinitions.BUFFER_SIZE];
        udpListener = new UDPListener(this, UDPDefinitions.UDP_PORT);
    }

    public void run()
    {
        udpListener.run();
    }

    public static UDPProcessor getInstance()
    {
        if(instance == null)
            instance = new UDPProcessor();
        return instance;
    }

    public void onPacketReceived(DatagramPacket receivedPacket, int localPort)
    {
        ByteBuffer buffer = ByteBuffer.allocate(receivedPacket.getLength());
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(receivedPacket.getData(), 0, receivedPacket.getLength());
        buffer.position(0);

        if(localPort == UDPDefinitions.UDP_PORT)
        {
            try
            {
                int channel = getIntFromChar((char) buffer.get(0));
                int octave = getIntFromChar((char) buffer.get(1));
                char note = (char) buffer.get(2);
                int volume = getIntFromChar((char) buffer.get(3));
                int length = getIntFromChar((char) buffer.get(4));

                SynthManager.playSynth(channel, octave, note, volume, length);
            }
            catch(Exception ignored)
            {
                Logger.log("Could not decode a UDP note", String.valueOf(Main.Template.DEBUG_WARNING));
            }
        }
    }

    private static int getIntFromChar(char input)
    {
        if(input >= 48 && input <= 57)
            return Integer.parseInt(String.valueOf(input));
        if(input >= 65 && input <= 90)
            return input - 55;
        if(input >= 97 && input <= 122)
            return input - 87;

        return 0;
    }
}