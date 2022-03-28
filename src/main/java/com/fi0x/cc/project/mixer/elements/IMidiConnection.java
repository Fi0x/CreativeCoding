package com.fi0x.cc.project.mixer.elements;

import javax.sound.midi.MidiDevice;

public interface IMidiConnection
{
    //TODO: Output incoming signals to a selected midi device or create new signals from midi-input devices
    boolean isOutput();
    boolean isInput();
    MidiDevice getConnectedMidi();
}
