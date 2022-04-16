package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.OldMixerUIElement;

@Deprecated
public class OldTimerElement extends OldAbstractMixerElement
{
    private int bpm = 60;
    private int notesPerBeat = 8;

    private int currentFrame = 0;

    public OldTimerElement(OldMixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(OldAbstractMixerElement sender, long globalFrame, int bpm)
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

        for(OldAbstractMixerElement e : connectedElements)
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
        return getUpdatedBpm();
    }
    public int getNotesPerBeat()
    {
        return notesPerBeat;
    }
    public int getCurrentFrame()
    {
        return currentFrame;
    }

    private int getUpdatedBpm()
    {
        int newValue = bpm;
        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e instanceof OldIncreasingElement)
                newValue += ((OldIncreasingElement) e).getCurrentIncrease();
        }
        return Math.min(Math.max(newValue, 1), 1000);
    }

    @Override
    public String getDisplayName()
    {
        return "BPM: " + getUpdatedBpm() + "(" + bpm + ")\nNPB: " + notesPerBeat;
    }
}
