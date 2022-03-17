package com.fi0x.cc.project.synth.UDP;

import com.fi0x.cc.project.synth.SynthPlayer;

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

                SynthPlayer.getInstance().playSynth(channel, octave, note, volume, length);
            }
            catch(Exception ignored)
            {
                System.out.println("Could not decode a UDP note");
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