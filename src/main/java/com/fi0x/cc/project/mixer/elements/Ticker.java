package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.mixer.MixerManager;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;

public class Ticker extends AbstractElement implements ISignalCreator, ISecondaryValues
{
    private int channel;
    private int notesPerBeat = 1;

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
    @Override
    public void beatUpdate(long frame)
    {
        if(nextLink == null)
            return;
        if(frame % (MixerManager.getNotesPerBeat() / notesPerBeat) != 0)
            return;

        ShortMessage msg = new ShortMessage();
        try
        {
            msg.setMessage(ShortMessage.NOTE_ON, 0, 60, 30);
            //TODO: Turn off notes after certain time (Check OldChannelElement)
        } catch(InvalidMidiDataException ignored)
        {
        }
        nextLink.receiveMidi(msg);

        sendPulse(nextLink, 1);
    }
    @Override
    public ArrayList<String> getSecondaryValueNames()
    {
        ArrayList<String> varNames = new ArrayList<>();

        varNames.add("Notes / Beat");

        return varNames;
    }
    @Override
    public void updateSecondaryValue(String valueName, int valueChange)
    {
        if(valueName.equals("Notes / Beat"))
        {
            notesPerBeat += valueChange;

            if(notesPerBeat < 1)
                notesPerBeat = 1;
            else if(notesPerBeat > MixerManager.getNotesPerBeat())
                notesPerBeat = MixerManager.getNotesPerBeat();
        }
    }
    @Override
    public String getDisplayString()
    {
        return "Ticker\n" + notesPerBeat;
    }
}
