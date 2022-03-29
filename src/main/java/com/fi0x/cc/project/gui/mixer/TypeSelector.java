package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.elements.*;
import controlP5.ControlEvent;
import controlP5.ControlP5;

import java.util.HashMap;
import java.util.Map;

public class TypeSelector
{
    private final MainMixerWindow parent;
    private int originalX;
    private int originalY;
    private final ControlP5 control;

    private final Map<String, Class<? extends AbstractElement>> possibleElements = new HashMap<>(){{
        put("Input", Input.class);
        put("Output", Output.class);
        put("SignalChanger", SignalChanger.class);
        put("Ticker", Ticker.class);
    }};

    public TypeSelector(MainMixerWindow parentScreen, int x, int y)
    {
        parent = parentScreen;
        originalX = x;
        originalY = y;
        control = new ControlP5(parent);

        createButtons();

        control.hide();
    }

    public boolean selectElement(ControlEvent event)
    {
        for(Map.Entry<String, Class<? extends AbstractElement>> entry : possibleElements.entrySet())
        {
            if(event.getController() == control.getController(entry.getKey()))
            {
                //TODO: Add new element if necessary to mainWindow
                //TODO: Create specified element at currentCenter
                //TODO: Remove this TypeSelector
                return true;
            }
        }

        if(event.getController() == control.getController("Abort"))
        {
            //TODO: Remove this TypeSelector
            return true;
        }

        return false;
    }

    public void hide()
    {
        control.hide();
    }
    public void show(int xPos, int yPos)
    {
        originalX = xPos;
        originalY = yPos;

        int y = possibleElements.size() / 2 * -21;
        for(String name : possibleElements.keySet())
        {
            control.getController(name)
                    .setPosition(originalX - 50, originalY + y);

            y += 21;
        }

        control.getController("Abort")
                .setPosition(originalX - 50, originalY + y);

        control.show();
    }

    private void createButtons()
    {
        int y = possibleElements.size() / 2 * -21;
        for(String name : possibleElements.keySet())
        {
            control.addButton(name)
                    .setSize(100, 20)
                    .setPosition(originalX - 50, originalY + y)
                    .setValue(0);

            y += 21;
        }

        control.addButton("Abort")
                .setSize(100, 20)
                .setPosition(originalX - 50, originalY + y)
                .setValue(0);
    }
}
