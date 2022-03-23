package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.project.gui.SoundVisualizer;
import com.fi0x.cc.project.gui.SynthUI;

import javax.sound.midi.MidiMessage;
import java.util.ArrayList;

public interface ISynthesizer
{
    void playNote(int octave, char note, int volume, int length);
    void sendMidiCommand(MidiMessage message);
    String getInstrumentName();
    void mute(boolean state);
    void nextInstrument();
    void previousInstrument();
    void setInstrument(String instrumentName);

    void linkUI(SynthUI uiElement);
    void linkVisualizer(SoundVisualizer visualizer);
}
