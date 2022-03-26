package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.synth.synthesizers.MusicConverter;

public class NoteElement extends AbstractMixerElement
{
    private int note = 60;

    public NoteElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long currentFrame, int bpm)
    {
        super.updateElement(currentFrame, bpm);
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
    public void syncClock(int timerFrame)
    {
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
