package com.fi0x.cc.project.synth;

import com.fi0x.cc.project.synth.UDP.UDPProcessor;
import com.fi0x.cc.project.synth.synthesizers.*;

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
        new Thread(() -> UDPProcessor.getInstance().run()).start();
        ((TestSynth) synthesizers.get(8)).waitForInstrumentChange();
    }

    public void registerAllSynths()
    {
        synthesizers.add(new BassSynth(synthesizers.size()));
        synthesizers.add(new CleanGuitarSynth(synthesizers.size()));
        synthesizers.add(new EPianoSynth(synthesizers.size()));
        synthesizers.add(new HarpSynth(synthesizers.size()));
        synthesizers.add(new JazzGuitarSynth(synthesizers.size()));
        synthesizers.add(new MarimbaSynth(synthesizers.size()));
        synthesizers.add(new OrganSynth(synthesizers.size()));
        synthesizers.add(new PianoSynth(synthesizers.size()));
        synthesizers.add(new TestSynth(synthesizers.size()));
        synthesizers.add(new TremoloSynth(synthesizers.size()));
        synthesizers.add(new VibraphoneSynth(synthesizers.size()));
        synthesizers.add(new ViolinSynth(synthesizers.size()));
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
