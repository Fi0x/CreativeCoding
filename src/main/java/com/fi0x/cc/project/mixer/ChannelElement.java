package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.synth.SynthManager;
import com.fi0x.cc.project.synth.synthesizers.MusicConverter;

public class ChannelElement extends AbstractMixerElement
{
    private int channel = 0;
    private int note = 60;
    private int volume = 30;
    private int noteLength = 1;

    public ChannelElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement()
    {
        super.updateElement();
        playNote();
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        channel += valueChange;
        if(channel < 0)
            channel = 15;
        if(channel > 15)
            channel = 0;
    }

    @Override
    public String getDisplayName()
    {
        return "Channel: " + channel;
    }

    private void playNote()
    {
        SynthManager.playSynth(channel, MusicConverter.getOctave(note), MusicConverter.getNoteName(note), volume, noteLength);
    }
}
