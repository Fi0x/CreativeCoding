package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.ShortMessage;
import java.util.ArrayList;

public class ClockCounter extends AbstractElement implements INumberProvider, ISecondaryValues
{
    private int notesBetweenUpdates = 1;
    private int minNumber = 0;
    private int maxNumber = 7;
    private int stepIncrease = 1;

    private int currentNumber;

    public ClockCounter(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);

        startColorAnimation();
    }

    @Override
    public void receiveMidi(ShortMessage msg)
    {
        Logger.log("A Clock counter should not receive midi signals", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        notesBetweenUpdates += valueChange;
        if(notesBetweenUpdates <= 0)
            notesBetweenUpdates = 1;
    }
    @Override
    public String getMainValueName()
    {
        return "Update Pause";
    }
    @Override
    public String getMainValue()
    {
        return String.valueOf(notesBetweenUpdates);
    }
    @Override
    public int getCurrentNumber()
    {
        return currentNumber;
    }
    @Override
    public void noteUpdate(long frame)
    {
        if(frame % notesBetweenUpdates != 0)
            return;

        currentNumber += stepIncrease;
        if(currentNumber > maxNumber)
            currentNumber = minNumber + (currentNumber - maxNumber) - 1;
        else if(currentNumber < minNumber)
            currentNumber = maxNumber - (minNumber - currentNumber);
    }
    @Override
    public ArrayList<String> getSecondaryValueNames()
    {
        ArrayList<String> varNames = new ArrayList<>();

        varNames.add("Min");
        varNames.add("Max");
        varNames.add("Step-size");

        return varNames;
    }
    @Override
    public void updateSecondaryValue(String valueName, int valueChange)
    {
        switch (valueName)
        {
            case "Min":
                minNumber += valueChange;
                if(minNumber > maxNumber)
                    minNumber = maxNumber;
                break;
            case "Max":
                maxNumber += valueChange;
                if(maxNumber < minNumber)
                    maxNumber = minNumber;
                break;
            case "Step-size":
                stepIncrease += valueChange;
                break;
        }
    }
    @Override
    public String getSecondaryValue(String valueName)
    {
        switch (valueName)
        {
            case "Min":
                return String.valueOf(minNumber);
            case "Max":
                return String.valueOf(maxNumber);
            case "Step-size":
                return String.valueOf(stepIncrease);
        }
        return "";
    }
    @Override
    public String getDisplayString()
    {
        return "ClockCounter\n" + currentNumber;
    }
}
