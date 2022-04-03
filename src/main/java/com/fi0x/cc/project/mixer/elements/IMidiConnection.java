package com.fi0x.cc.project.mixer.elements;

import javax.sound.midi.MidiDevice;

public interface IMidiConnection
{
    MidiDevice getConnectedMidi();
}
