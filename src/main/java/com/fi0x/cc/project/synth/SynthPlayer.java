package com.fi0x.cc.project.synth;

import com.fi0x.cc.project.synth.UDP.UDPProcessor;
import com.fi0x.cc.project.synth.synthesizers.ISynthesizer;
import com.fi0x.cc.project.synth.synthesizers.PianoSynth;
import java.util.ArrayList;

public class SynthPlayer
{
    private static SynthPlayer instance;
    private final ArrayList<ISynthesizer> synthesizers = new ArrayList<>();

    private SynthPlayer()
    {
        registerAllSynths();
    }

    public static SynthPlayer getInstance()
    {
        if(instance == null)
            instance = new SynthPlayer();
        return instance;
    }

    public void startListening()
    {
        UDPProcessor.getInstance().run();
    }

    public void registerAllSynths()
    {
        synthesizers.add(new PianoSynth(synthesizers.size()));
    }

    public void playSynth(int deviceChannel, int octave, char note, int volume, int length)
    {
        ISynthesizer synth = getSynth(deviceChannel);
        if(synth == null)
            return;

        new Thread(() -> synth.playNote(octave, note, volume, length)).start();
    }

    private ISynthesizer getSynth(int channel)
    {
        if(synthesizers.size() - 1 < channel)
            return null;

        return synthesizers.get(channel);
    }
}
