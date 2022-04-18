package com.fi0x.cc.project.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import java.util.List;

public class CustomMidi implements MidiDevice
{
    @Override
    public Info getDeviceInfo() {
        return null;
    }

    @Override
    public void open(){

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public long getMicrosecondPosition() {
        return 0;
    }

    @Override
    public int getMaxReceivers() {
        return 0;
    }

    @Override
    public int getMaxTransmitters() {
        return 0;
    }

    @Override
    public Receiver getReceiver(){
        return null;
    }

    @Override
    public List<Receiver> getReceivers() {
        return null;
    }

    @Override
    public Transmitter getTransmitter(){
        return null;
    }

    @Override
    public List<Transmitter> getTransmitters() {
        return null;
    }
}
