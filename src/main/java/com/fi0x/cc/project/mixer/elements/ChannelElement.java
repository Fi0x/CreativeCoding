package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;
import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.TimeCalculator;
import com.fi0x.cc.project.synth.SynthManager;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;

public class ChannelElement extends AbstractMixerElement
{
    private long lastNotePlayed = 0;

    private int channel = 0;
    private final ArrayList<Integer> notes = new ArrayList<>();
    private int volume = 30;
    private int noteLength = 2 * MixerManager.getNotesPerBeat() / 8;

    public ChannelElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long globalFrame, int bpm)
    {
        if(updatedFrames.contains(globalFrame))
            return;
        updatedFrames.add(globalFrame);

        super.updateElement(globalFrame, bpm);

        notes.clear();
        for(AbstractMixerElement e : connectedElements)
        {
            if(e instanceof NoteElement)
            {
                e.updateElement(globalFrame, bpm);
                notes.add(((NoteElement) e).getNote());
            } else if(e instanceof PitchElement)
            {
                e.updateElement(globalFrame, bpm);
                ((PitchElement) e).updateChannelPitch(channel);
            } else if(e instanceof VolumeElement)
            {
                e.updateElement(globalFrame, bpm);
                volume = ((VolumeElement) e).getVolume();
            } else if(e instanceof LengthElement)
            {
                e.updateElement(globalFrame, bpm);
                noteLength = ((LengthElement) e).getLength();
            }
        }

        playNotes();
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
    public void changeSecondaryValue(int valueChange)
    {
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

    private void playNotes()
    {
        lastNotePlayed = System.currentTimeMillis();

        for(int note : notes)
        {
            new Thread(() ->
            {
                int currentChannel = channel;
                try
                {
                    ShortMessage msg = new ShortMessage();
                    msg.setMessage(ShortMessage.NOTE_ON, currentChannel, note, volume);
                    SynthManager.handleMidiCommand(msg);
                } catch(InvalidMidiDataException e)
                {
                    return;
                }

                int delay = TimeCalculator.getMillisFromBeat(noteLength);
                try
                {
                    Thread.sleep(delay);
                } catch(InterruptedException ignored)
                {
                }

                if(lastNotePlayed > System.currentTimeMillis() - delay && currentChannel == channel)
                    return;

                try
                {
                    ShortMessage msg = new ShortMessage();
                    msg.setMessage(ShortMessage.NOTE_OFF, currentChannel, note, 0);
                    SynthManager.handleMidiCommand(msg);
                } catch(InvalidMidiDataException e)
                {
                    Logger.log("Could not stop midi note", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
                }
            }).start();
        }
    }
}
