package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.elements.AbstractElement;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class ElementUI
{
    private final MainMixerWindow parent;
    protected int currentX;
    protected int currentY;

    private boolean isSelected;
    private boolean pickedUp = false;

    private int currentBackgroundColor;
    private int currentStrokeColor;
    private float colorReverseRate;

    public ElementUI(MainMixerWindow parentScreen, int x, int y)
    {
        parent = parentScreen;

        currentX = x;
        currentY = y;
        isSelected = false;

        currentBackgroundColor = UIConstants.DEFAULT_BACKGROUND;
        currentStrokeColor = UIConstants.DEFAULT_STROKE;
    }

    public void draw()
    {
        if(pickedUp)
        {
            currentX = parent.mouseX;
            currentY = parent.mouseY;
        }

        parent.stroke(isSelected ? UIConstants.SELECTED_STROKE_COLOR : currentStrokeColor);
        parent.strokeWeight(5);
        parent.fill(currentBackgroundColor);
        parent.ellipse(currentX, currentY, UIConstants.ELEMENT_SIZE, UIConstants.ELEMENT_SIZE);
        parent.noStroke();

        parent.fill(UIConstants.DEFAULT_TEXT);
        parent.textSize(14);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text("Not\nfinished", currentX, currentY);

        currentStrokeColor = parent.lerpColor(currentStrokeColor, UIConstants.DEFAULT_STROKE, colorReverseRate);
        currentBackgroundColor = parent.lerpColor(currentBackgroundColor, UIConstants.DEFAULT_BACKGROUND, colorReverseRate);
    }
    public void drawConnectionLines(int color)
    {
        parent.stroke(color);
        parent.strokeWeight(5);

        for(AbstractElement e : parent.getActiveElements())
            drawLine(e);

        parent.noStroke();
    }
    public void blinkColor(int blinkColor, float resetRate)
    {
        colorReverseRate = resetRate;
        currentBackgroundColor = blinkColor;
    }
    public void blinkStroke(int blinkColor, float resetRate)
    {
        colorReverseRate = resetRate;
        currentStrokeColor = blinkColor;
    }

    public boolean isAbove(float x, float y)
    {
        float distance = PApplet.dist(x, y, currentX, currentY);
        return !(distance > UIConstants.ELEMENT_SIZE / 2f);
    }
    public void select(boolean selected)
    {
        isSelected = selected;
    }
    public boolean pickUp()
    {
        float distance = PApplet.dist(parent.mouseX, parent.mouseY, currentX, currentY);
        if(distance > UIConstants.ELEMENT_SIZE / 2f)
            return false;

        pickedUp = true;
        ((AbstractElement) this).removeAllConnections();

        return true;
    }
    public void drop()
    {
        //TODO: find closest output node
        //TODO: find closest existing node with the same output node in 180° angle towards output node
        //TODO: connect with that node
        for(AbstractElement e : parent.getActiveElements())
            tryToConnect(e);

        pickedUp = false;
    }
    public void sendPulse(ElementUI target, float travelTime)
    {
        int transferFrames = (int) (travelTime * parent.frameRate);
        PVector src = new PVector(currentX, currentY);
        PVector dst = new PVector(target.currentX, target.currentY);
        parent.addUISignal(new UISignal(parent, src, dst, transferFrames, currentBackgroundColor));
    }

    private void tryToConnect(AbstractElement otherElement)
    {
        if(((AbstractElement) this).getConnectedOutput() != null || !otherElement.hasFreeInputs())
            return;

        if(0 < UIConstants.MAX_CONNECTION_DIST && otherElement != this)
        {
            //TODO: Add links
        }
    }
    private void drawLine(AbstractElement otherUI)
    {
        float dist = PApplet.dist(currentX, currentY, otherUI.currentX, otherUI.currentY);
        if(dist < UIConstants.MAX_CONNECTION_DIST)
            parent.line(currentX, currentY, otherUI.currentX, otherUI.currentY);
    }
}
