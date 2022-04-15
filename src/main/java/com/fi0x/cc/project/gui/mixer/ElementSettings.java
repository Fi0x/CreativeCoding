package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.elements.*;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class ElementSettings
{
    PApplet parent;
    private int originalX;
    private int originalY;
    private final AbstractElement link;

    private int updatedSize = UIConstants.ELEMENT_SIZE;

    private final ArrayList<SettingsCircle> circles = new ArrayList<>();

    //TODO: Expand the element circle and display sub-circles inside with the different settings
    public ElementSettings(MainMixerWindow parentScreen, int x, int y, AbstractElement linkedElement)
    {
        parent = parentScreen;
        originalX = x;
        originalY = y;
        link = linkedElement;

        //TODO: Think of a solution to close the menu again
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
    public void updateSetting(int xPos, int yPos, int valueChange)
    {
        //TODO: Check which setting is at mouse position and change correct value
    }
    public AbstractElement getLinkedElement()
    {
        return link;
    }

    private void createCircles()
    {
        ArrayList<String> valueNames = new ArrayList<>();
        valueNames.add(link.getMainValueName());
        if(link instanceof ISecondaryValues)
            valueNames.addAll(((ISecondaryValues) link).getSecondaryValueNames());

        float circlePis = 2f / valueNames.size();
        PVector circleOffset = new PVector(0, -UIConstants.SETTINGS_ELEMENT_SIZE);
        updatedSize = UIConstants.ELEMENT_SIZE + UIConstants.SETTINGS_ELEMENT_SIZE;

        for(String name : valueNames)
        {
            circles.add(new SettingsCircle(name, circleOffset));
            circleOffset.rotate(PConstants.PI * circlePis);
        }
    }

    private class SettingsCircle
    {
        private String name;
        private int xOffset;
        private int yOffset;

        private SettingsCircle(String valueName, PVector offset)
        {
            name = valueName;
            xOffset = (int) offset.x;
            yOffset = (int) offset.y;
        }

        private void drawCircle()
        {
            parent.fill(UIConstants.SETTINGS_ELEMENT_BACKGROUND);
            parent.ellipse(xOffset, yOffset, UIConstants.SETTINGS_ELEMENT_SIZE, UIConstants.SETTINGS_ELEMENT_SIZE);
            parent.fill(UIConstants.DEFAULT_TEXT);
            parent.text(name, xOffset, yOffset);
        }
    }
}
