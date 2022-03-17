package com.fi0x.cc.project.synth.UDP;

import java.net.DatagramPacket;

public interface UDPPacketProcessor
{
    void onPacketReceived(DatagramPacket receivedPacket, int localPort);
}
