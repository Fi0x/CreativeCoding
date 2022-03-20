package com.fi0x.cc.project.synth;

import com.fi0x.cc.exercises.Startup;
import com.fi0x.cc.project.synth.UDP.UDPProcessor;
import com.fi0x.cc.project.synth.midi.MidiHandler;
import com.fi0x.cc.project.synth.synthesizers.*;
import io.fi0x.javalogger.logging.Logger;

import java.util.ArrayList;
import java.util.Scanner;

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
        new MidiHandler();
        new Thread(() -> UDPProcessor.getInstance().run()).start();
        while(true)
        {
            Scanner sc = new Scanner(System.in);
            String input = sc.next();
            boolean state = input.equals("0");
            for(ISynthesizer synth : synthesizers)
                synth.mute(state);
        }
    }

    public void registerAllSynths()
    {
        synthesizers.add(new BassSynth(synthesizers.size()));           //0
        synthesizers.add(new CleanGuitarSynth(synthesizers.size()));    //1
        synthesizers.add(new EPianoSynth(synthesizers.size()));         //2
        synthesizers.add(new HarpSynth(synthesizers.size()));           //3
        synthesizers.add(new JazzGuitarSynth(synthesizers.size()));     //4
        synthesizers.add(new MarimbaSynth(synthesizers.size()));        //5
        synthesizers.add(new OrganSynth(synthesizers.size()));          //6
        synthesizers.add(new PianoSynth(synthesizers.size()));          //7
        synthesizers.add(new TremoloSynth(synthesizers.size()));        //8
        synthesizers.add(new VibraphoneSynth(synthesizers.size()));     //9
        synthesizers.add(new ViolinSynth(synthesizers.size()));         //a
        synthesizers.add(new ChoirSynth(synthesizers.size()));          //b
        synthesizers.add(new Drum1Synth(synthesizers.size()));          //c
        synthesizers.add(new Drum2Synth(synthesizers.size()));          //d
        //e
        //f

        Logger.getInstance().log("Loaded midi devices on channels 0-" + (synthesizers.size() - 1), String.valueOf(Startup.LogTemplate.INFO_GREEN));
        printSynthList();
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

    private void printSynthList()
    {
        for(int i = 0; i < synthesizers.size(); i++)
            Logger.getInstance().log("\t" + Integer.toHexString(i) + "\t" + synthesizers.get(i).getInstrumentName(), String.valueOf(Startup.LogTemplate.INFO_PURPLE));
    }
}
