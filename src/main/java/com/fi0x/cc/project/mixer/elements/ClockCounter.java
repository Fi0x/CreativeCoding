package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.Main;
import com.fi0x.cc.project.mixer.gui.MainMixerWindow;
import com.fi0x.cc.project.mixer.MixerSignal;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.INumberProvider;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISecondaryValues;
import io.fi0x.javalogger.logging.Logger;

import java.util.ArrayList;

public class ClockCounter extends AbstractElement implements INumberProvider, ISecondaryValues
{
    private int notesBetweenUpdates = 1;
    private int minNumber = 0;
    private int maxNumber = 7;
    private int stepIncrease = 1;
    private boolean bounceOnEdges = false;

    private int currentNumber;
    private boolean inverted;

    public ClockCounter(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);

        startColorAnimation();
    }

    @Override
    public void receiveMidi(MixerSignal msg)
    {
        Logger.log("A Clock counter should not receive midi signals", String.valueOf(Main.Template.DEBUG_WARNING));
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

        if((currentNumber == maxNumber || currentNumber == minNumber) && bounceOnEdges)
            inverted = !inverted;

        currentNumber += inverted ? -stepIncrease : stepIncrease;
        if(currentNumber > maxNumber)
                currentNumber = minNumber + (currentNumber - maxNumber) - 1;
        else if(currentNumber < minNumber)
                currentNumber = maxNumber - (minNumber - currentNumber) + 1;
    }
    @Override
    public ArrayList<String> getSecondaryValueNames()
    {
        ArrayList<String> varNames = new ArrayList<>();

        varNames.add("Min");
        varNames.add("Max");
        varNames.add("Step-size");
        varNames.add("Mode");

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
            case "Mode":
                bounceOnEdges = valueChange > 0;
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
            case "Mode":
                return bounceOnEdges ? "Bounce" : "Clock";
        }
        return "";
    }
    @Override
    public String getDisplayString()
    {
        return "ClockCounter\n" + currentNumber;
    }
}
