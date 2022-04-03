package com.fi0x.cc.project.synth;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.synth.synthesizers.*;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Collections;

public class SynthManager
{
    //TODO: Add option to select a specific midi device and only listen to than one

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

        synths.add(new BassSynth(0));
        synths.add(new ChoirSynth(1));
        synths.add(new CleanGuitarSynth(2));
        synths.add(new ChurchOrganSynth(3));
        synths.add(new SaxSynth(4));
        synths.add(new EPianoSynth(5));
        synths.add(new HarpSynth(6));
        synths.add(new JazzGuitarSynth(7));
        synths.add(new MarimbaSynth(8));
        synths.add(new EmptySynth(9));
        synths.add(new OrganSynth(10));
        synths.add(new PianoSynth(11));
        synths.add(new TremoloSynth(12));
        synths.add(new VibraphoneSynth(13));
        synths.add(new ViolinSynth(14));
        synths.add(new PlaceholderSynth(15));

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
        {
            if(i.toString().startsWith("Drumkit"))
                continue;
            list.add(i.toString().split("bank")[0]);
        }

        Collections.sort(list);

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
    public static int getProgramNumber(String midiName, String... alternativeName)
    {
        int number = getProgramNumber(midiName);
        for (String nextName : alternativeName)
        {
            if(number >= 0)
                break;
            number = getProgramNumber(nextName);
        }
        return number;
    }
}
