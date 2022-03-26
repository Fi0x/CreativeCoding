package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.*;
import processing.core.PApplet;
import processing.core.PConstants;

public class MixerUIElement
{
    private final PApplet parent;
    protected int currentX;
    protected int currentY;
    private final int size = 120;

    private final int backgroundColor;
    private final int selectionColor;
    private final int textColor;
    private int adjustableColor;

    private boolean isSelected = false;
    private boolean pickedUp = false;
    private final int connectionDistance = 300;

    private AbstractMixerElement linkedElement;

    public MixerUIElement(PApplet parentScreen, int xCenter, int yCenter)
    {
        parent = parentScreen;

        currentX = xCenter;
        currentY = yCenter;

        backgroundColor = parent.color(255, 0, 0);
        selectionColor = parent.color(0, 255, 255);
        textColor = parent.color(0);
        adjustableColor = backgroundColor;

        linkedElement = new ChannelElement(this);
    }

    public void init()
    {
    }
    public void drawElement()
    {
        if(pickedUp)
        {
            currentX = parent.mouseX;
            currentY = parent.mouseY;
        }

        if(isSelected)
        {
            parent.stroke(selectionColor);
            parent.strokeWeight(3);
        }
        parent.fill(adjustableColor);
        parent.ellipse(currentX, currentY, size, size);
        parent.noStroke();

        parent.fill(textColor);
        parent.textSize(18);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text(linkedElement.getDisplayName(), currentX, currentY);

        adjustableColor = parent.lerpColor(adjustableColor, backgroundColor, 0.03f);
    }
    public void drawLines()
    {
        drawConnectionLines();
    }
    public void blinkColor()
    {
        adjustableColor = parent.color(255);
    }

    public boolean isAbove()
    {
        float distance = PApplet.dist(parent.mouseX, parent.mouseY, currentX, currentY);
        return !(distance > size / 2f);
    }
    public boolean pickUp()
    {
        float distance = PApplet.dist(parent.mouseX, parent.mouseY, currentX, currentY);
        if(distance > size / 2f)
            return false;

        pickedUp = true;
        linkedElement.removeAllConnections();

        return true;
    }
    public void drop()
    {
        tryToConnect(MainMixerWindow.originElement);
        for(MixerUIElement e : MainMixerWindow.uiElements)
            tryToConnect(e);

        pickedUp = false;
    }
    public void select(boolean selected)
    {
        isSelected = selected;
    }

    public void updateLocation(int newX, int newY)
    {
        currentX = newX;
        currentY = newY;
    }

    public AbstractMixerElement getLinkedElement()
    {
        return linkedElement;
    }
    public void selectNextElement()
    {
        if(ChannelElement.class.equals(linkedElement.getClass()))
            linkedElement = new OscillatorElement(this);
        else if(OscillatorElement.class.equals(linkedElement.getClass()))
            linkedElement = new PitchElement(this);
        else if(PitchElement.class.equals(linkedElement.getClass()))
            linkedElement = new VolumeElement(this);
        else if(VolumeElement.class.equals(linkedElement.getClass()))
            linkedElement = new ChannelElement(this);
    }
    public void changeLinkedElement(AbstractMixerElement newElement)
    {
        linkedElement = newElement;
    }

    private void tryToConnect(MixerUIElement otherElement)
    {
        float distance = PApplet.dist(otherElement.currentX, otherElement.currentY, currentX, currentY);

        if(distance < connectionDistance)
        {
            otherElement.getLinkedElement().addConnectedElement(linkedElement);
            linkedElement.addConnectedElement(otherElement.getLinkedElement());
        }
    }
    private void drawConnectionLines()
    {
        parent.fill(255);
        parent.stroke(255);

        int otherX = MainMixerWindow.originElement.currentX;
        int otherY = MainMixerWindow.originElement.currentY;
        float distance = PApplet.dist(otherX, otherY, currentX, currentY);

        if(distance < connectionDistance)
            parent.line(otherX, otherY, currentX, currentY);

        for(MixerUIElement e : MainMixerWindow.uiElements)
        {
            distance = PApplet.dist(e.currentX, e.currentY, currentX, currentY);
            if(distance < connectionDistance)
                parent.line(e.currentX, e.currentY, currentX, currentY);
        }

        parent.noStroke();
    }
}
