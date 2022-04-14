package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;

import javax.sound.midi.ShortMessage;

public class SignalChanger extends AbstractElement implements ISignalModifier
{
    public SignalChanger(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);

        startColorAnimation();
    }
    @Override
    public void receiveMidi(ShortMessage msg)
    {
        if(nextLink == null)
            return;

        nextLink.receiveMidi(changeMidiMessage(msg));
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        //TODO: select which value to change
    }
    //TODO: Add secondary value to determine the amount to change

    private ShortMessage changeMidiMessage(ShortMessage originalMsg)
    {
        //TODO: Change channel, volume, note or length of originalMsg and return result
        return originalMsg;
    }

    @Override
    public String getDisplayString()
    {
        String additionalInfo = "Missing infos";
        return "Signal Changer\n" + additionalInfo;
    }
}
