package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.mixer.gui.MainMixerWindow;
import com.fi0x.cc.project.midi.MidiSignalHelper;
import com.fi0x.cc.project.midi.MidiSignalInfo;
import com.fi0x.cc.project.mixer.MixerSignal;
import com.fi0x.cc.project.mixer.TimeCalculator;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.INumberProvider;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISecondaryValues;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISignalModifier;

import javax.sound.midi.ShortMessage;
import java.util.*;

public class SignalChanger extends AbstractElement implements ISignalModifier, ISecondaryValues
{
    private MidiSignalParts valueToChange = MidiSignalParts.Channel;
    private boolean increment = true;
    private int changeAmount = 0;

    private final Map<UUID, Status> handledSignals = new HashMap<>();

    private final ArrayList<INumberProvider> connectedNumberProviders = new ArrayList<>();

    public SignalChanger(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);

        startColorAnimation();
    }
    @Override
    public void receiveMidi(MixerSignal msg)
    {
        if(nextLink == null)
            return;

        if(connectedNumberProviders.size() > 0)
            updateChangeAmount();

        final MixerSignal threadSafeSignal = msg;

        new Thread(() ->
        {
            if(!handledSignals.containsKey(threadSafeSignal.id))
                handledSignals.put(threadSafeSignal.id, new Status());

            Status correctStatus = handledSignals.get(threadSafeSignal.id);
            threadSafeSignal.midiMessage = changeMidiMessage(threadSafeSignal);
            if(correctStatus.part == MidiSignalParts.Length)
            {
                if(Objects.requireNonNull(MidiSignalInfo.getSignalType(threadSafeSignal.midiMessage.getStatus())).NAME == MidiSignalInfo.MidiSignalName.NoteOff)
                {
                    try
                    {
                        Thread.sleep(TimeCalculator.getMillisFromBeat(correctStatus.amount));
                    } catch(InterruptedException ignored)
                    {
                    }
                }
            }
            nextLink.receiveMidi(threadSafeSignal);
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
        if(!connectedNumberProviders.contains(provider))
            connectedNumberProviders.add(provider);
    }
    @Override
    public void removeNumberProvider(INumberProvider provider)
    {
        connectedNumberProviders.remove(provider);
    }
    @Override
    public String getDisplayImageName()
    {
        return super.getDisplayImageName();//TODO: Return a useful image
    }

    private ShortMessage changeMidiMessage(MixerSignal originalMsg)
    {
        MidiSignalInfo messageInfo = MidiSignalInfo.getSignalType(originalMsg.midiMessage.getStatus());
        assert messageInfo != null;
        if(messageInfo.NAME == MidiSignalInfo.MidiSignalName.ProgramChange || messageInfo.NAME == MidiSignalInfo.MidiSignalName.ControlChange)
            return originalMsg.midiMessage;

        Status originalStatus = handledSignals.get(originalMsg.id);
        switch(originalStatus.part)
        {
            case Channel:
                MidiSignalHelper.changeChannel(originalMsg.midiMessage, originalStatus.amount, originalStatus.inc);
                break;
            case Note:
                MidiSignalHelper.changeNote(originalMsg.midiMessage, originalStatus.amount, originalStatus.inc);
                break;
            case Volume:
                MidiSignalHelper.changeVolume(originalMsg.midiMessage, originalStatus.amount, originalStatus.inc);
                break;
            case CombinedData:
                MidiSignalHelper.changeAllData(originalMsg.midiMessage, originalStatus.amount, originalStatus.inc);
                break;
        }
        return originalMsg.midiMessage;
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
    private class Status
    {
        private final MidiSignalParts part;
        private final boolean inc;
        private final int amount;

        private Status()
        {
            part = valueToChange;
            inc = increment;
            amount = changeAmount;
        }
    }
}
