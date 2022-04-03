package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.ElementUI;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.mixer.elements.ISignalCreator;

import java.util.ArrayList;

public class MixerManager implements Runnable
{
    private static MixerManager instance;
    private final ArrayList<ISignalCreator> beatListeners = new ArrayList<>();

    private long globalFrame = 0;
    private static int bpm = 60;
    private static int notesPerBeat = 8;

    private MixerManager()
    {
    }

    @Override
    public void run()
    {
        while(true)
        {
            if(globalFrame % notesPerBeat == 0)
            {
                for(ElementUI e : MainMixerWindow.getActiveElements())
                    e.updateConnections();
            }
            for(ISignalCreator s : beatListeners)
                s.beatUpdate(globalFrame);

            try
            {
                Thread.sleep(60000 / bpm / notesPerBeat);
            } catch(InterruptedException ignored)
            {
                break;
            }

            globalFrame++;
        }
    }

    public static MixerManager getInstance()
    {
        if(instance == null)
            instance = new MixerManager();

        return instance;
    }

    public void addBeatListener(ISignalCreator listener)
    {
        beatListeners.add(listener);
    }

    public void removeBeatListener(ISignalCreator listener)
    {
        beatListeners.remove(listener);
    }

    public static int getBPM()
    {
        return bpm;
    }
    public static int getNotesPerBeat()
    {
        return notesPerBeat;
    }
    public static void changeBPM(int bpmChange)
    {
        bpm += bpmChange;
        if(bpm < 1)
            bpm = 1;
    }
    public static void changeNPB(int npbChange)
    {
        notesPerBeat += npbChange;
        if(notesPerBeat < 1)
            notesPerBeat = 1;
    }
}
