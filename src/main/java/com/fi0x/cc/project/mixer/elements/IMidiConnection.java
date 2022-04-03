package com.fi0x.cc.project.mixer.elements;

import javax.sound.midi.MidiDevice;

public interface IMidiConnection
{
    boolean isOutput();
    boolean isInput();
    MidiDevice getConnectedMidi();
}
