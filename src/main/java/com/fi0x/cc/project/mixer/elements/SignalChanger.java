package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.midi.MidiSignalHelper;
import com.fi0x.cc.project.midi.MidiSignalInfo;

import javax.sound.midi.ShortMessage;
import java.util.ArrayList;

public class SignalChanger extends AbstractElement implements ISignalModifier, ISecondaryValues
{
    private MidiSignalHelper.MidiSignalParts valueToChange = MidiSignalHelper.MidiSignalParts.Channel;
    private boolean increment = false;
    private int changeAmount = 0;

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
        MidiSignalHelper.MidiSignalParts[] partValues = MidiSignalHelper.MidiSignalParts.values();
        int newPartIdx = valueToChange.ordinal() + valueChange;
        if(newPartIdx < 0)
            newPartIdx = 0;
        else if(newPartIdx >= partValues.length)
            newPartIdx = partValues.length - 1;

        valueToChange = partValues[newPartIdx];
    }
    @Override
    public String getMainValueName()
    {
        return "Value-type";
    }
    @Override
    public String getMainValue()
    {
        return valueToChange.toString();
    }

    private ShortMessage changeMidiMessage(ShortMessage originalMsg)
    {
        MidiSignalInfo messageInfo = MidiSignalInfo.getSignalType(originalMsg.getStatus());
        assert messageInfo != null;
        if(messageInfo.NAME == MidiSignalInfo.MidiSignalName.ProgramChange || messageInfo.NAME == MidiSignalInfo.MidiSignalName.ControlChange)
            return originalMsg;

        switch(valueToChange)
        {
            case Channel:
                MidiSignalHelper.changeChannel(originalMsg, changeAmount, increment);
                break;
            case Note:
                MidiSignalHelper.changeNote(originalMsg, changeAmount, increment);
                break;
            case Volume:
                MidiSignalHelper.changeVolume(originalMsg, changeAmount, increment);
                break;
            case CombinedData:
                MidiSignalHelper.changeAllData(originalMsg, changeAmount, increment);
                break;
        }
        return originalMsg;
    }

    @Override
    public String getDisplayString()
    {
        String additionalInfo = valueToChange.toString();
        return "Signal Changer\n" + additionalInfo;
    }
    @Override
    public ArrayList<String> getSecondaryValueNames()
    {
        ArrayList<String> varNames = new ArrayList<>();

        varNames.add("Behaviour");
        varNames.add("Amount");

        return varNames;
    }
    @Override
    public void updateSecondaryValue(String valueName, int valueChange)
    {
        switch (valueName)
        {
            case "Behaviour":
                increment = valueChange > 0;
                break;
            case "Amount":
                changeAmount += valueChange;
                break;
        }
    }
    @Override
    public String getSecondaryValue(String valueName)
    {
        switch (valueName)
        {
            case "Behaviour":
                return increment ? "Incremental" : "Absolute";
            case "Amount":
                return String.valueOf(changeAmount);
        }
        return "";
    }
}
