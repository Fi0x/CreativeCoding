package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.mixer.AbstractMixerElement;
import com.fi0x.cc.project.synth.SynthManager;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class PitchElement extends AbstractMixerElement
{
    private int pitchDifference = 0;

    public PitchElement(MixerUIElement uiPart)
    {
        super(uiPart);
    }

    @Override
    public void updateElement(long globalFrame, int bpm)
    {
        super.updateElement(globalFrame, bpm);

        allowedConnections.add(IncreasingElement.class);
        allowedConnections.add(ChannelElement.class);
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        pitchDifference += valueChange;
        if(pitchDifference < -128)
            pitchDifference = -128;
        else if(pitchDifference > 127)
            pitchDifference = 127;
    }
    @Override
    public void changeSecondaryValue(int valueChange)
    {
    }
    @Override
    public void syncClock(int timerFrame)
    {
    }

    public void updateChannelPitch(int channel)
    {
        int updatedPitch = pitchDifference;
        for(AbstractMixerElement e : connectedElements)
        {
            if(e instanceof IncreasingElement)
                updatedPitch += ((IncreasingElement) e).getCurrentIncrease();
        }
        updatedPitch = Math.max(-128, Math.min(127, updatedPitch));
        try
        {
            //TODO: Use correct values
            String value = Integer.toBinaryString((updatedPitch + 128) * 64);
            while(value.length() < 14)
                value = "0" + value;

            System.out.println(value);

            int lsb = Integer.parseInt(value.substring(6, 7), 2);
            int msb = Integer.parseInt(value.substring(0, 7), 2);

            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.PITCH_BEND, channel, msb, lsb);
            SynthManager.handleMidiCommand(msg);
        } catch(InvalidMidiDataException ignored)
        {
        }
    }

    @Override
    public String getDisplayName()
    {
        return "Pitch: " + pitchDifference;
    }
}
