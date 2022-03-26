package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;
import com.fi0x.cc.project.synth.synthesizers.MusicConverter;

public class NoteElement extends AbstractMixerElement
{
    private int note = 60;

    public NoteElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long globalFrame, int bpm)
    {
        super.updateElement(globalFrame, bpm);
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        note += valueChange;

        if(note < 0)
            note = 0;
        else if(note > 127)
            note = 127;
    }
    @Override
    public void changeSecondaryValue(int valueChange)
    {
    }
    @Override
    public void syncClock(int timerFrame)
    {
    }
    @Override
    public boolean canConnectTo(AbstractMixerElement otherElement)
    {
        return otherElement instanceof ChannelElement;
    }

    public int getNote()
    {
        return note;
    }

    @Override
    public String getDisplayName()
    {
        return "Note: " + MusicConverter.getOctave(note) + MusicConverter.getNoteName(note);
    }
}
