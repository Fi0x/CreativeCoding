package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISecondaryValues;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ElementSettings
{
    PApplet parent;
    private int originalX;
    private int originalY;
    private final AbstractElement link;

    private int updatedSize = UIConstants.ELEMENT_SIZE;

    private final ArrayList<SettingsCircle> circles = new ArrayList<>();

    public ElementSettings(MainMixerWindow parentScreen, int x, int y, AbstractElement linkedElement)
    {
        parent = parentScreen;
        originalX = x;
        originalY = y;
        link = linkedElement;

        createCircles();
    }

    public void hide()
    {
        link.closeMenu();
    }
    public void show(int xPos, int yPos)
    {
        originalX = xPos;
        originalY = yPos;

        link.openMenu(this);
    }
    public void draw()
    {
        parent.translate(originalX, originalY);

        for(SettingsCircle c : circles)
            c.drawCircle();

        parent.translate(-originalX, -originalY);
    }

    public int getUpdatedSize()
    {
        return updatedSize;
    }
    public void updateSetting(int mouseX, int mouseY, int valueChange)
    {
        for(SettingsCircle c : circles)
        {
            if(c.isAbove(mouseX, mouseY))
            {
                if(c.type == SettingType.MainValue)
                    link.changeMainValue(valueChange);
                else if(c.type == SettingType.SecondaryValue)
                    ((ISecondaryValues) link).updateSecondaryValue(c.name, valueChange);
                break;
            }
        }
    }
    public AbstractElement getLinkedElement()
    {
        return link;
    }

    private void createCircles()
    {
        Map<String, SettingType> valueNames = new HashMap<>();
        valueNames.put(link.getMainValueName(), SettingType.MainValue);
        if(link instanceof ISecondaryValues)
        {
            for(String name : ((ISecondaryValues) link).getSecondaryValueNames())
                valueNames.put(name, SettingType.SecondaryValue);
        }

        float circlePis = 2f / valueNames.size();
        PVector circleOffset = new PVector(0, -(float) (UIConstants.ELEMENT_SIZE + UIConstants.SETTINGS_ELEMENT_SIZE) / 2);
        updatedSize = (int) (UIConstants.ELEMENT_SIZE + UIConstants.SETTINGS_ELEMENT_SIZE * 1.5);

        for(Map.Entry<String, SettingType> setting : valueNames.entrySet())
        {
            circles.add(new SettingsCircle(setting.getKey(), circleOffset, setting.getValue()));
            circleOffset.rotate(PConstants.PI * circlePis);
        }
    }

    private class SettingsCircle
    {
        private final String name;
        private final SettingType type;
        private final int xOffset;
        private final int yOffset;

        private SettingsCircle(String valueName, PVector offset, SettingType type)
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

            String text = name;
            if(type == SettingType.MainValue)
                text += "\n" + link.getMainValue();
            else if(type == SettingType.SecondaryValue)
                text += "\n" + ((ISecondaryValues) link).getSecondaryValue(name);

            parent.fill(UIConstants.DEFAULT_TEXT);
            parent.text(text, xOffset, yOffset);
        }

        private boolean isAbove(int x, int y)
        {
            float distance = PApplet.dist(x, y, originalX + xOffset, originalY + yOffset);

            return !(distance > UIConstants.SETTINGS_ELEMENT_SIZE / 2f);
        }
    }
    private enum SettingType
    {
        MainValue,
        SecondaryValue
    }
}
