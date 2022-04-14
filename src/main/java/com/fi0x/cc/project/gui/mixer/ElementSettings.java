package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.elements.*;
import controlP5.ControlEvent;
import controlP5.ControlP5;

import java.util.HashMap;
import java.util.Map;

public class ElementSettings
{
    private int originalX;
    private int originalY;
    private final ControlP5 control;
    private final AbstractElement link;

    private final Map<CommandObject, ICommand> possibleElements = new HashMap<>();

    public ElementSettings(MainMixerWindow parentScreen, int x, int y, AbstractElement linkedElement)
    {
        originalX = x;
        originalY = y;
        control = new ControlP5(parentScreen);
        control.setPosition(0, 0);
        link = linkedElement;

        if(linkedElement instanceof ISecondaryValues)
        {
            for(String name : ((ISecondaryValues) linkedElement).getSecondaryValueNames())
            {
                possibleElements.put(new CommandObject(name + "-", name, -1), new ChangeValue());
                possibleElements.put(new CommandObject(name + "+", name, 1), new ChangeValue());
            }
        }

        createButtons();

        control.hide();
    }

    public int clickButton(ControlEvent event)
    {
        for(Map.Entry<CommandObject, ICommand> entry : possibleElements.entrySet())
        {
            if(event.getController() == control.getController(entry.getKey().buttonName))
            {
                entry.getValue().execute(entry.getKey());
                return 1;
            }
        }

        return event.getController() == control.getController("Back") ? 0 : -1;
    }

    public void hide()
    {
        control.hide();
        new Thread(() ->
        {
            try
            {
                Thread.sleep(50);
            } catch(InterruptedException ignored)
            {
            }
            control.dispose();
        }).start();
    }
    public void show(int xPos, int yPos)
    {
        originalX = xPos;
        originalY = yPos;

        int y = possibleElements.size() / 2 * -21;
        for(CommandObject name : possibleElements.keySet())
        {
            control.getController(name.buttonName)
                    .setPosition(originalX - 50, originalY + y);

            y += 21;
        }

        control.getController("Back")
                .setPosition(originalX - 50, originalY + y);

        control.show();
    }

    private void createButtons()
    {
        int y = possibleElements.size() / 2 * -21;
        for(CommandObject name : possibleElements.keySet())
        {
            control.addButton(name.buttonName)
                    .setSize(100, 20)
                    .setPosition(originalX - 50, originalY + y)
                    .setValue(0);

            y += 21;
        }

        control.addButton("Back")
                .setSize(100, 20)
                .setPosition(originalX - 50, originalY + y)
                .setValue(0);
    }

    private interface ICommand
    {
        void execute(CommandObject data);
    }
    private class ChangeValue implements ICommand
    {
        @Override
        public void execute(CommandObject data)
        {
            ((ISecondaryValues) link).updateSecondaryValue(data.valueName, data.valueChange);
        }
    }
    private class CommandObject
    {
        private final String buttonName;
        private final String valueName;
        private final int valueChange;

        private CommandObject(String buttonName, String valueName, int valueChange)
        {
            this.buttonName = buttonName;
            this.valueName = valueName;
            this.valueChange = valueChange;
        }
    }
}
