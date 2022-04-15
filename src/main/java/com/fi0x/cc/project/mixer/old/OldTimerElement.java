package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.OldMixerUIElement;

@Deprecated
public class OldTimerElement extends OldAbstractMixerElement
{
    private int bpm = 60;

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
        int notesPerBeat = 8;
        if(currentFrame % notesPerBeat == 0)
        {
            currentFrame = 0;
        }

        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e == sender)
                continue;
            e.updateElement(this, globalFrame, bpm);
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

}
