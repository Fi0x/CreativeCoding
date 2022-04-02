package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.mixer.elements.*;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import io.fi0x.javalogger.logging.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        control.setPosition(0, 0);

        createButtons();

        control.hide();
    }

    public boolean selectElement(ControlEvent event)
    {
        for(Map.Entry<String, Class<? extends AbstractElement>> entry : possibleElements.entrySet())
        {
            if(event.getController() == control.getController(entry.getKey()))
            {
                try
                {
                    Constructor<? extends AbstractElement> ctor = entry.getValue().getDeclaredConstructor(MainMixerWindow.class, Integer.class, Integer.class);
                    parent.addNewElement(ctor.newInstance(parent, originalX, originalY));
                } catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e)
                {
                    Logger.log("Could not create element from type-selector", String.valueOf(LoggerManager.Template.DEBUG_WARNING));
                }
                return true;
            }
        }

        return event.getController() == control.getController("Abort");
    }

    public void hide()
    {
        control.hide();
        new Thread(() ->
        {
            try
            {
                Thread.sleep(100);
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
