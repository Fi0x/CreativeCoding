package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.OldMixerUIElement;

@Deprecated
public class OldVolumeElement extends OldAbstractMixerElement implements IParameterElement
{
    private int volume = 30;

    public OldVolumeElement(OldMixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(OldAbstractMixerElement sender, long globalFrame, int bpm)
    {
        super.updateElement(sender, globalFrame, bpm);

        allowedConnections.add(OldIncreasingElement.class);
        allowedConnections.add(OldChannelElement.class);
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        volume += valueChange;
        if(volume < 0)
            volume = 0;
        if(volume > 127)
            volume = 127;
    }
    @Override
    public void changeSecondaryValue(int valueChange)
    {
    }
    @Override
    public void syncClock(int timerFrame)
    {
    }

    public int getVolume()
    {
        int updatedVolume = volume;
        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e instanceof OldIncreasingElement)
                updatedVolume += ((OldIncreasingElement) e).getCurrentIncrease();
        }
        return Math.max(Math.min(updatedVolume, 127), 0);
    }

    @Override
    public String getDisplayName()
    {
        return "Volume: " + getVolume() + "(" + volume + ")";
    }
}
