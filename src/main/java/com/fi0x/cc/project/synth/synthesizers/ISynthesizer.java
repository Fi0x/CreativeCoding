package com.fi0x.cc.project.synth.synthesizers;

public interface ISynthesizer
{
    void playNote(int octave, char note, int volume, int length);
}
