package com.fi0x.cc.project.mixer.abstractinterfaces;

import javax.sound.midi.MidiDevice;

public interface IMidiConnection
{
    MidiDevice getConnectedMidi();
}
