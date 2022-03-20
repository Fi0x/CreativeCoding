package com.fi0x.cc.project.synth.synthesizers;

public interface ISynthesizer
{
    void playNote(int octave, char note, int volume, int length);
    String getInstrumentName();
    void mute(boolean state);
    void nextInstrument();
    void previousInstrument();
}
