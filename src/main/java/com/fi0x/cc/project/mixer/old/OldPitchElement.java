package com.fi0x.cc.project.mixer.old;

import com.fi0x.cc.project.gui.mixer.MixerUIElement;
import com.fi0x.cc.project.synth.SynthManager;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

@Deprecated
public class OldPitchElement extends OldAbstractMixerElement implements IParameterElement
{
    private int pitchDifference = 0;

    public OldPitchElement(MixerUIElement uiPart)
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
        int updatedPitch = Math.max(-128, Math.min(127, getUpdatedPitch()));
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

    private int getUpdatedPitch()
    {
        int newValue = pitchDifference;
        for(OldAbstractMixerElement e : connectedElements)
        {
            if(e instanceof OldIncreasingElement)
                newValue += ((OldIncreasingElement) e).getCurrentIncrease();
        }
        return Math.max(Math.min(newValue, 127), -128);
    }

    @Override
    public String getDisplayName()
    {
        return "Pitch: " + getUpdatedPitch() + "(" + pitchDifference + ")";
    }
}
