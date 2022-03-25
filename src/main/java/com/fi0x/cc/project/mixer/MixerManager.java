package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.AbstractMixerUIElement;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

public class MixerManager implements Runnable
{
    private static MixerManager instance;
    private int updatesPerSecond = 60;

    private MixerManager()
    {
    }

    @Override
    public void run()
    {
        while(true)
        {
            for(AbstractMixerUIElement uiElement : MainMixerWindow.uiElements)
                uiElement.getLinkedElement().updateElement();

            try
            {
                Thread.sleep(1000 / updatesPerSecond);
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

    public void setFramerate(int updatesPerSecond)
    {
        this.updatesPerSecond = updatesPerSecond;
    }
}
