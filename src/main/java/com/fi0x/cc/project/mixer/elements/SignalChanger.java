package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.midi.MidiSignalHelper;
import com.fi0x.cc.project.midi.MidiSignalInfo;
import com.fi0x.cc.project.mixer.TimeCalculator;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.INumberProvider;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISecondaryValues;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISignalModifier;

import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.Objects;

public class SignalChanger extends AbstractElement implements ISignalModifier, ISecondaryValues
{
    private MidiSignalParts valueToChange = MidiSignalParts.Channel;
    private boolean increment = false;
    private int changeAmount = 0;

    private final ArrayList<INumberProvider> connectedNumberProviders = new ArrayList<>();

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

        new Thread(() ->
        {
            ShortMessage updatedMessage = changeMidiMessage(msg);
            if(valueToChange == MidiSignalParts.Length)
            {
                if(Objects.requireNonNull(MidiSignalInfo.getSignalType(msg.getStatus())).NAME == MidiSignalInfo.MidiSignalName.NoteOff)
                {
                    try
                    {
                        Thread.sleep(TimeCalculator.getMillisFromBeat(changeAmount));
                    } catch(InterruptedException ignored)
                    {
                    }
                }
            }
            nextLink.receiveMidi(updatedMessage);
        }).start();
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        MidiSignalParts[] partValues = MidiSignalParts.values();
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
    @Override
    public void addNumberProvider(INumberProvider provider)
    {
        connectedNumberProviders.add(provider);
    }
    @Override
    public void removeNumberProvider(INumberProvider provider)
    {
        connectedNumberProviders.remove(provider);
    }

    private ShortMessage changeMidiMessage(ShortMessage originalMsg)
    {
        if(connectedNumberProviders.size() > 0)
            updateChangeAmount();

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
    private void updateChangeAmount()
    {
        changeAmount = 0;
        for(INumberProvider np : connectedNumberProviders)
            changeAmount += np.getCurrentNumber();
    }

    public enum MidiSignalParts
    {
        Channel,
        Note,
        Volume,
        CombinedData,
        Length
    }
}
