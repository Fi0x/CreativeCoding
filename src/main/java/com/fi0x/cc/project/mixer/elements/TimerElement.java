package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;

public class TimerElement extends AbstractMixerElement
{
    private int bpm = 60;
    private int notesPerBeat = 8;

    private int currentFrame = 0;

    public TimerElement(MixerUIElement uiPart)
    {
        super(uiPart);

        allowedConnections.add(ChannelElement.class);
        allowedConnections.add(DelayElement.class);
        allowedConnections.add(IncreasingElement.class);
        allowedConnections.add(IntervalElement.class);
        allowedConnections.add(TickElement.class);
    }

    @Override
    public void updateElement(AbstractMixerElement sender, long globalFrame, int bpm)
    {
        if(updatedFrames.contains(globalFrame))
            return;
        updatedFrames.add(globalFrame);

        currentFrame++;
        if(currentFrame % notesPerBeat == 0)
        {
            currentFrame = 0;
            linkedUI.blinkColor(0.03f * bpm / 60);
        }

        for(AbstractMixerElement e : connectedElements)
        {
            if(e == sender)
                continue;
            e.updateElement(this, globalFrame, bpm);
            linkedUI.sendPulse(e.getLinkedUI(), 1);
        }
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        bpm += valueChange;
        if(bpm < 1)
            bpm = 1;
        if(bpm > 1000)
            bpm = 1000;
    }
    @Override
    public void changeSecondaryValue(int valueChange)
    {
        notesPerBeat += valueChange;
        if(notesPerBeat < 1)
            notesPerBeat = 1;
        if(notesPerBeat > 128)
            notesPerBeat = 128;
    }
    @Override
    public void syncClock(int timerFrame)
    {
    }

    public int getCurrentBPM()
    {
        return bpm;
    }
    public int getNotesPerBeat()
    {
        return notesPerBeat;
    }
    public int getCurrentFrame()
    {
        return currentFrame;
    }

    @Override
    public String getDisplayName()
    {
        return "BPM: " + bpm + ", " + notesPerBeat;
    }
}
