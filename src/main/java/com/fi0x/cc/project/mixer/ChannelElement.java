package com.fi0x.cc.project.mixer;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.synth.SynthManager;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class ChannelElement extends AbstractMixerElement
{
    private long lastUpdatedFrame = 0;
    private long lastNotePlayed = 0;

    private int channel = 0;
    private int note = 60;
    private int volume = 30;
    private int noteLength = 1000;

    public ChannelElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long currentFrame, int bpm)
    {
        if(lastUpdatedFrame == currentFrame)
            return;
        lastUpdatedFrame = currentFrame;

        super.updateElement(currentFrame, bpm);

        for(AbstractMixerElement e : connectedElements)
        {
            if(e instanceof NoteElement)
            {
                e.updateElement(currentFrame, bpm);
                note = ((NoteElement) e).getNote();
            } else if(e instanceof PitchElement)
            {
                e.updateElement(currentFrame, bpm);
                ((PitchElement) e).updateChannelPitch(channel);
            } else if(e instanceof VolumeElement)
            {
                e.updateElement(currentFrame, bpm);
                volume = ((VolumeElement) e).getVolume();
            }
        }

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
    public void syncClock(int timerFrame)
    {
    }

    @Override
    public String getDisplayName()
    {
        return "Channel: " + channel;
    }

    private void playNote()
    {
        lastNotePlayed = System.currentTimeMillis();
        new Thread(() ->
        {
            int currentChannel = channel;
            int currentNote = note;
            try
            {
                ShortMessage msg = new ShortMessage();
                msg.setMessage(ShortMessage.NOTE_ON, currentChannel, currentNote, volume);
                SynthManager.handleMidiCommand(msg);
            } catch(InvalidMidiDataException e)
            {
                return;
            }

            try
            {
                Thread.sleep(noteLength);
            } catch(InterruptedException ignored)
            {
            }

            if(lastNotePlayed > System.currentTimeMillis() - noteLength)
                return;

            try
            {
                ShortMessage msg = new ShortMessage();
                msg.setMessage(ShortMessage.NOTE_OFF, currentChannel, currentNote, 0);
                SynthManager.handleMidiCommand(msg);
            } catch(InvalidMidiDataException e)
            {
                Logger.log("Could not stop midi note", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
            }
        }).start();
    }
}
