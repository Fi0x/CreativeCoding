package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;
import com.fi0x.cc.project.mixer.IParameterElement;
import com.fi0x.cc.project.synth.synthesizers.MusicConverter;

public class NoteElement extends AbstractMixerElement implements IParameterElement
{
    private int note = 60;

    public NoteElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(AbstractMixerElement sender, long globalFrame, int bpm)
    {
        super.updateElement(sender, globalFrame, bpm);

        allowedConnections.add(IncreasingElement.class);
        allowedConnections.add(ChannelElement.class);
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

    public int getNote()
    {
        int newNote = note;
        for(AbstractMixerElement e : connectedElements)
        {
            if(e instanceof IncreasingElement)
                newNote += ((IncreasingElement) e).getCurrentIncrease();
        }
        return newNote % 128;
    }

    @Override
    public String getDisplayName()
    {
        return "Note: " + MusicConverter.getOctave(note) + MusicConverter.getNoteName(note);
    }
}
