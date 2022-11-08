package com.fi0x.cc.project.mixer.gui;

import com.fi0x.cc.Main;
import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISignalCreator;
import com.fi0x.cc.project.mixer.elements.*;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TypeSelector
{
    MainMixerWindow parent;
    private final int originalX;
    private final int originalY;

    private final Map<String, Class<? extends AbstractElement>> possibleElements = new HashMap<>(){{
        put("Input", Input.class);
        put("Output", Output.class);
        put("SignalChanger", SignalChanger.class);
        put("Ticker", Ticker.class);
        put("ClockCounter", ClockCounter.class);
        put("SignalMuter", SignalMuter.class);
        put("Abort", null);
    }};
    private final ArrayList<TypeCircle> circles = new ArrayList<>();

    public TypeSelector(MainMixerWindow parentScreen, int x, int y)
    {
        parent = parentScreen;
        originalX = x;
        originalY = y;

        createCircles();
    }

    public boolean selectElement(int x, int y)
    {
        for(TypeCircle entry : circles)
        {
            if(entry.isAbove(x, y))
            {
                if(entry.getType() == null)
                    return true;

                try
                {
                    Constructor<? extends AbstractElement> ctor = entry.getType().getDeclaredConstructor(MainMixerWindow.class, int.class, int.class);
                    AbstractElement inst = ctor.newInstance(parent, originalX, originalY);
                    parent.addNewElement(inst);
                    if(inst instanceof ISignalCreator)
                        MixerManager.getInstance().addBeatListener((ISignalCreator) inst);
                    inst.updateConnections();
                } catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e)
                {
                    Logger.log("Could not create element from type-selector", String.valueOf(Main.Template.DEBUG_WARNING));
                }
                return true;
            }
        }

        return false;
    }

    public void draw()
    {
        parent.pushMatrix();
        parent.translate(originalX, originalY);

        for(TypeCircle c : circles)
            c.drawCircle();

        parent.popMatrix();
    }

    private void createCircles()
    {
        float circlePis = 2f / possibleElements.size();
        PVector circleOffset = new PVector(0, -(float) (UIConstants.ELEMENT_SIZE + UIConstants.SETTINGS_ELEMENT_SIZE) / 2);

        for(Map.Entry<String, Class<? extends AbstractElement>> setting : possibleElements.entrySet())
        {
            circles.add(new TypeCircle(setting.getKey(), circleOffset, setting.getValue()));
            circleOffset.rotate(PConstants.PI * circlePis);
        }
    }

    private class TypeCircle
    {
        private final String name;
        private final Class<? extends AbstractElement> type;
        private final int xOffset;
        private final int yOffset;

        private TypeCircle(String valueName, PVector offset, Class<? extends AbstractElement> type)
        {
            name = valueName;
            xOffset = (int) offset.x;
            yOffset = (int) offset.y;
            this.type = type;
        }

        private void drawCircle()
        {
            parent.fill(UIConstants.SETTINGS_ELEMENT_BACKGROUND);
            parent.ellipse(xOffset, yOffset, UIConstants.SETTINGS_ELEMENT_SIZE, UIConstants.SETTINGS_ELEMENT_SIZE);

            parent.fill(UIConstants.DEFAULT_TEXT);
            parent.text(name, xOffset, yOffset);
        }

        private boolean isAbove(int x, int y)
        {
            float distance = PApplet.dist(x, y, originalX + xOffset, originalY + yOffset);

            return !(distance > UIConstants.SETTINGS_ELEMENT_SIZE / 2f);
        }

        private Class<? extends AbstractElement> getType()
        {
            return type;
        }
    }
}
