package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.synth.synthesizers.MusicConverter;

@Deprecated
public class OldNoteElement extends OldAbstractMixerElement implements IParameterElement
{
    private int note = 60;

    public OldNoteElement(MixerUIElement uiPart)
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
        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e instanceof OldIncreasingElement)
                newNote += ((OldIncreasingElement) e).getCurrentIncrease();
        }
        return Math.max(Math.min(newNote, 127), 0);
    }

    @Override
    public String getDisplayName()
    {
        int n = getNote();
        return "Note: " + MusicConverter.getOctave(n) + MusicConverter.getNoteName(n);
    }
}
