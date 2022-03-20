package com.fi0x.cc.project.synth;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.synth.synthesizers.*;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.*;
import java.util.ArrayList;

public class SynthManager
{
    private static ArrayList<ISynthesizer> synths;
    private static int nextSynthToLoad = 0;

    private static Instrument[] instruments;
    private static MidiChannel[] channels;

    private static void registerSynths() throws MidiUnavailableException
    {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();

        synth.loadAllInstruments(synth.getDefaultSoundbank());
        channels = synth.getChannels();
        instruments = synth.getLoadedInstruments();

        synths = new ArrayList<>();

        synths.add(new BassSynth(synths.size()));
        synths.add(new ChoirSynth(synths.size()));
        synths.add(new CleanGuitarSynth(synths.size()));
        synths.add(new Drum1Synth(synths.size()));
        synths.add(new Drum2Synth(synths.size()));
        synths.add(new EPianoSynth(synths.size()));
        synths.add(new HarpSynth(synths.size()));
        synths.add(new JazzGuitarSynth(synths.size()));
        synths.add(new MarimbaSynth(synths.size()));
        synths.add(new EmptySynth(synths.size()));
        synths.add(new OrganSynth(synths.size()));
        synths.add(new PianoSynth(synths.size()));
        synths.add(new TremoloSynth(synths.size()));
        synths.add(new VibraphoneSynth(synths.size()));
        synths.add(new ViolinSynth(synths.size()));
        synths.add(new PlaceholderSynth(synths.size()));

        Logger.log("Loaded synths", String.valueOf(LoggerManager.Template.DEBUG_INFO));
    }

    public static ISynthesizer getNextSynth()
    {
        if(synths == null)
        {
            try
            {
                registerSynths();
            } catch(MidiUnavailableException e)
            {
                Logger.log("Could not load midi-devices", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
                return null;
            }
        }

        assert synths != null;
        if(nextSynthToLoad >= synths.size())
            nextSynthToLoad = 0;

        nextSynthToLoad++;
        return synths.get(nextSynthToLoad - 1);
    }

    public static void playSynth(int deviceChannel, int octave, char note, int volume, int length)
    {
        if(synths.size() - 1 < deviceChannel)
            return;

        ISynthesizer synth = synths.get(deviceChannel);
        if(synth == null)
            return;

        new Thread(() -> synth.playNote(octave, note, volume, length)).start();
    }
    public static void handleMidiCommand(MidiMessage msg)
    {
        String status = Integer.toBinaryString(msg.getStatus());
        int midiChannel = Integer.parseInt(status.substring(4), 2);
        if(midiChannel >= synths.size())
            return;
        synths.get(midiChannel).sendMidiCommand(msg);
    }

    public static String getInstrumentName(int programNumber)
    {
        return instruments[programNumber].toString().split("bank")[0];
    }
    public static String[] getAllInstrumentNames()
    {
        ArrayList<String> list = new ArrayList<>();
        for(Instrument i : instruments)
            list.add(i.toString().split("bank")[0]);

        return list.toArray(new String[0]);
    }
    public static MidiChannel getChannel(int channelNumber)
    {
        if(channels == null || channels.length == 0)
            return null;
        return channels[Math.min(channelNumber, SynthManager.channels.length - 1)];
    }
    public static int getProgramNumber(String midiName)
    {
        for(int i = 0; i < SynthManager.instruments.length; i++)
        {
            if(instruments[i].toString().startsWith(midiName))
                return i;
        }

        return -1;
    }
}
