package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.midi.MidiSignalHelper;
import com.fi0x.cc.project.midi.MidiSignalInfo;

import javax.sound.midi.ShortMessage;

public class SignalChanger extends AbstractElement implements ISignalModifier
{
    private MidiSignalHelper.MidiSignalParts valueToChange = MidiSignalHelper.MidiSignalParts.Channel;

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
    //TODO: Add secondary value to determine the amount to change

    private ShortMessage changeMidiMessage(ShortMessage originalMsg)
    {
        MidiSignalInfo messageInfo = MidiSignalInfo.getSignalType(originalMsg.getStatus());
        assert messageInfo != null;
        if(messageInfo.NAME == MidiSignalInfo.MidiSignalName.ProgramChange || messageInfo.NAME == MidiSignalInfo.MidiSignalName.ControlChange)
            return originalMsg;

        switch(valueToChange)
        {
            //TODO: Use correct values instead of 0
            case Channel:
                MidiSignalHelper.changeChannel(originalMsg, 0);
                break;
            case Note:
                MidiSignalHelper.changeNote(originalMsg, 0);
                break;
            case Volume:
                MidiSignalHelper.changeVolume(originalMsg, 0);
                break;
            case CombinedData:
                MidiSignalHelper.changeAllData(originalMsg, 0);
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
}
