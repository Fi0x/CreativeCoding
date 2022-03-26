package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

public class MixerManager implements Runnable
{
    private static MixerManager instance;
    private long currentFrame = 0;

    private MixerManager()
    {
    }

    @Override
    public void run()
    {
        while(true)
        {
            int currentBPM = ((TimerElement) MainMixerWindow.originElement.getLinkedElement()).getCurrentBPM();
            MainMixerWindow.originElement.getLinkedElement().updateElement(currentFrame, currentBPM);

            try
            {
                Thread.sleep(1000 / ((TimerElement) MainMixerWindow.originElement.getLinkedElement()).getCurrentBPM());
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
}
