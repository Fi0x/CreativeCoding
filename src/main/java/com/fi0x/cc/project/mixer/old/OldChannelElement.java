package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.mixer.OldMixerUIElement;
import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.TimeCalculator;
import com.fi0x.cc.project.synth.SynthManager;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;

@Deprecated
public class OldChannelElement extends OldAbstractMixerElement
{
    private long lastNotePlayed = 0;

    private int channel = 0;
    private final ArrayList<Integer> notes = new ArrayList<>();
    private int volume = 30;
    private int noteLength = 2 * MixerManager.getNotesPerBeat() / 8;

    public OldChannelElement(OldMixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(OldAbstractMixerElement sender, long globalFrame, int bpm)
    {
        if(updatedFrames.contains(globalFrame))
            return;
        updatedFrames.add(globalFrame);

        super.updateElement(sender, globalFrame, bpm);

        notes.clear();
        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e instanceof OldNoteElement)
            {
                e.updateElement(this, globalFrame, bpm);
                notes.add(((OldNoteElement) e).getNote());
            } else if(e instanceof OldPitchElement)
            {
                e.updateElement(this, globalFrame, bpm);
                ((OldPitchElement) e).updateChannelPitch(getUpdatedChannel());
            } else if(e instanceof OldVolumeElement)
            {
                e.updateElement(this, globalFrame, bpm);
                volume = ((OldVolumeElement) e).getVolume();
            } else if(e instanceof OldLengthElement)
            {
                e.updateElement(this, globalFrame, bpm);
                noteLength = ((OldLengthElement) e).getLength();
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
    public boolean canConnectTo(OldAbstractMixerElement otherElement)
    {
        return true;
    }

    @Override
    public String getDisplayName()
    {
        return "Channel: " + getUpdatedChannel() + "(" + channel + ")";
    }

    private void playNotes()
    {
        lastNotePlayed = System.currentTimeMillis();

        for(int note : notes)
        {
            new Thread(() ->
            {
                int currentChannel = getUpdatedChannel();
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

                if(lastNotePlayed > System.currentTimeMillis() - delay && currentChannel == getUpdatedChannel())
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
    private int getUpdatedChannel()
    {
        int newChannel = channel;
        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e instanceof OldIncreasingElement)
                newChannel += ((OldIncreasingElement) e).getCurrentIncrease();
        }
        return Math.max(newChannel % 16, channel);
    }
}
