package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.mixer.elements.TimerElement;

public class MixerManager implements Runnable
{
    private static MixerManager instance;

    private long currentFrame = 0;
    private int bpm = 60;
    private int notesPerBeat = 8;

    private MixerManager()
    {
    }

    @Override
    public void run()
    {
        TimerElement masterClock = ((TimerElement) MainMixerWindow.originElement.getLinkedElement());
        while(true)
        {
            bpm = masterClock.getCurrentBPM();
            notesPerBeat = masterClock.getNotesPerBeat();
            masterClock.updateElement(null, currentFrame, bpm);

            try
            {
                Thread.sleep(60000 / bpm / notesPerBeat);
            } catch(InterruptedException ignored)
            {
                break;
            }

            currentFrame++;
        }
    }

    public static MixerManager getInstance()
    {
        if(instance == null)
            instance = new MixerManager();

        return instance;
    }

    public static int getBPM()
    {
        return getInstance().bpm;
    }
    public static int getNotesPerBeat()
    {
        return getInstance().notesPerBeat;
    }
}
