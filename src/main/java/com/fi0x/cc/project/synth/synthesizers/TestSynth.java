package com.fi0x.cc.project.synth.synthesizers;

import com.fi0x.cc.Startup;
import io.fi0x.javalogger.LogSettings;

import javax.sound.midi.*;
import java.util.Scanner;

public class TestSynth implements ISynthesizer
{
    private MidiChannel channel;
    private Instrument[] instruments;
    private int currentInstrument = 46;

    public TestSynth(int mappedChannel)
    {
        try
        {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();

            synth.loadAllInstruments (synth.getDefaultSoundbank());
            MidiChannel[] channels = synth.getChannels();
            channel = channels[Math.min(mappedChannel, channels.length - 1)];

            instruments = synth.getLoadedInstruments();
            nextInstrument();
        } catch (MidiUnavailableException ignored)
        {
        }
    }

    public void waitForInstrumentChange()
    {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            String input = sc.next();
            if(input.equals("1"))
                nextInstrument();
            else if(input.equals("-1"))
                previousInstrument();
        }
    }
    private void nextInstrument()
    {
        channel.allNotesOff();
        currentInstrument++;
        if(currentInstrument >= instruments.length)
            currentInstrument = 0;

        LogSettings.getLOGFromTemplate("Switching to new instrument " + currentInstrument + ": " + instruments[currentInstrument], String.valueOf(Startup.LogTemplate.DEBUG_INFO));
        channel.programChange(currentInstrument);
    }
    private void previousInstrument()
    {
        channel.allNotesOff();
        currentInstrument--;
        if(currentInstrument < 0)
            currentInstrument = instruments.length - 1;

        LogSettings.getLOGFromTemplate("Switching to new instrument " + currentInstrument + ": " + instruments[currentInstrument], String.valueOf(Startup.LogTemplate.DEBUG_INFO));
        channel.programChange(currentInstrument);
    }

    @Override
    public void playNote(int octave, char note, int volume, int length)
    {
        channel.noteOn(MusicConverter.getNoteValue(note, octave), volume);
        try
        {
            Thread.sleep(length);
        } catch(InterruptedException ignored)
        {
        }
        channel.noteOff(note);
    }

    @Override
    public String getInstrumentName()
    {
        return "Testing";
    }
}
