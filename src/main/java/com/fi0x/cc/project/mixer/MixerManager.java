package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.mixer.elements.AbstractElement;
import com.fi0x.cc.project.mixer.elements.ISignalCreator;
import com.fi0x.cc.project.mixer.elements.Input;

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
                for(AbstractElement e : MainMixerWindow.getActiveElements())
                {
                    e.updateConnections();
                    e.sendPulse(e.getConnectedNode(), 2);
                }
            }
            for(ISignalCreator s : beatListeners)
                s.noteUpdate(globalFrame);

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
        if(listener instanceof Input)
            return;

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
        int currentPower = (int) (Math.log(notesPerBeat) / Math.log(2));
        currentPower += npbChange;
        if(currentPower < 0)
            currentPower = 0;

        notesPerBeat = (int) Math.pow(2, currentPower);
    }
}
