package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

public class MixerManager implements Runnable
{
    private static MixerManager instance;

    private MixerManager()
    {
    }

    @Override
    public void run()
    {
        while(true)
        {
            MainMixerWindow.originElement.getLinkedElement().updateElement();

            try
            {
                Thread.sleep(1000 / ((TimerElement) MainMixerWindow.originElement.getLinkedElement()).getCurrentBPM());
            } catch(InterruptedException ignored)
            {
                break;
            }
        }
    }

    public static MixerManager getInstance()
    {
        if(instance == null)
            instance = new MixerManager();

        return instance;
    }
}
