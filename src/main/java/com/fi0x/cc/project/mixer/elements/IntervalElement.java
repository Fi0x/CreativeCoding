package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;
import com.fi0x.cc.project.mixer.MixerManager;

public class IntervalElement extends AbstractMixerElement
{
    private int intervalLength = 8 * MixerManager.getNotesPerBeat() / 8;
    private int currentFrame = 0;
    private boolean isActive = false;

    public IntervalElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long globalFrame, int bpm)
    {
        if(updatedFrames.contains(globalFrame))
            return;
        updatedFrames.add(globalFrame);

        currentFrame++;
        if(currentFrame % intervalLength == 0)
            isActive = !isActive;

        if(isActive)
        {
            super.updateElement(globalFrame, bpm);
            for(AbstractMixerElement e : connectedElements)
                e.updateElement(globalFrame, bpm);
        }
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        intervalLength += valueChange;
        if(intervalLength < 1)
            intervalLength = 1;
    }
    @Override
    public void changeSecondaryValue(int valueChange)
    {
    }
    @Override
    public void syncClock(int timerFrame)
    {
        currentFrame = timerFrame;
    }
    @Override
    public boolean canConnectTo(AbstractMixerElement otherElement)
    {
        return !(otherElement instanceof LengthElement)
                && !(otherElement instanceof NoteElement)
                && !(otherElement instanceof PitchElement)
                && !(otherElement instanceof VolumeElement);
    }

    @Override
    public String getDisplayName()
    {
        return "Interval: " + intervalLength;
    }
}
