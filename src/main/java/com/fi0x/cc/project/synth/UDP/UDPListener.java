package com.fi0x.cc.project.synth.UDP;

import com.fi0x.cc.exercises.Startup;
import com.fi0x.cc.project.LoggerManager;
import io.fi0x.javalogger.logging.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPListener implements Runnable
{
    int port;
    byte[] receiveData;
    DatagramSocket udpListeningSocket;
    UDPProcessor processor;
    public UDPListener(UDPProcessor listener,int localPort)
    {
        port = localPort;
        this.processor = listener;
        receiveData = new byte[UDPDefinitions.BUFFER_SIZE];
        try
        {
            udpListeningSocket = new DatagramSocket(port);
        } catch (SocketException ignored)
        {
        }
    }

    @Override
    public void run()
    {
        Logger.log("Listening for UDP Packets on port " + UDPDefinitions.UDP_PORT, String.valueOf(LoggerManager.Template.DEBUG_INFO));
        while(true)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try
            {
                udpListeningSocket.receive(receivePacket);
                processor.onPacketReceived(receivePacket, port);

            } catch (IOException ignored)
            {
            }
        }
    }
}