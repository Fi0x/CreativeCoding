package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.ShortMessage;

public class Ticker extends AbstractElement implements ISignalCreator
{
    private int channel;

    public Ticker(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);
    }
    @Override
    public void receiveMidi(ShortMessage msg)
    {
        Logger.log("Ticker received a midi message, but should not have any input connections", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        channel += valueChange;

        if(channel < 0)
            channel = 0;
        else if(channel > 15)
            channel = 15;
    }
    //TODO: Add secondary value for tick frequency
    @Override
    public void beatUpdate(long frame)
    {
        if(nextLink == null)
            return;

        //TODO: Create pulses after x notes / beats that can be modified further
        ShortMessage msg = new ShortMessage();
        nextLink.receiveMidi(msg);

        sendPulse(nextLink, 1);
    }
}
