package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.*;
import com.fi0x.cc.project.mixer.elements.*;
import processing.core.PApplet;
import processing.core.PConstants;

public class MixerUIElement
{
    private final PApplet parent;
    protected int currentX;
    protected int currentY;
    private final int size = 100;

    private final int backgroundColor;
    private final int selectionColor;
    private final int textColor;
    private final int strokeColor;
    private int adjustableBackgroundColor;
    private int adjustableStrokeColor;
    private float colorReverseRate;

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
        selectionColor = parent.color(0, 0, 255);
        textColor = parent.color(0);
        strokeColor = parent.color(0, 0, 0, 0);
        adjustableBackgroundColor = backgroundColor;
        adjustableStrokeColor = strokeColor;

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

        parent.stroke(isSelected ? selectionColor : adjustableStrokeColor);
        parent.strokeWeight(3);
        parent.fill(adjustableBackgroundColor);
        parent.ellipse(currentX, currentY, size, size);
        parent.noStroke();

        parent.fill(textColor);
        parent.textSize(18);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text(linkedElement.getDisplayName(), currentX, currentY);

        adjustableStrokeColor = parent.lerpColor(adjustableStrokeColor, strokeColor, colorReverseRate);
        adjustableBackgroundColor = parent.lerpColor(adjustableBackgroundColor, backgroundColor, colorReverseRate);
    }
    public void drawLines()
    {
        drawConnectionLines();
    }
    public void blinkColor(float resetRate)
    {
        colorReverseRate = resetRate;
        adjustableBackgroundColor = parent.color(255);
    }
    public void blinkStroke(float resetRate)
    {
        colorReverseRate = resetRate;
        adjustableStrokeColor = parent.color(0, 255, 0);
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

        linkedElement.syncClock(((TimerElement) MainMixerWindow.originElement.getLinkedElement()).getCurrentFrame());

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
        AbstractMixerElement oldElement = linkedElement;

        if(ChannelElement.class.equals(linkedElement.getClass()))
            linkedElement = new DelayElement(this);
        else if(DelayElement.class.equals(linkedElement.getClass()))
            linkedElement = new IntervalElement(this);
        else if(IntervalElement.class.equals(linkedElement.getClass()))
            linkedElement = new LengthElement(this);
        else if(LengthElement.class.equals(linkedElement.getClass()))
            linkedElement = new NoteElement(this);
        else if(NoteElement.class.equals(linkedElement.getClass()))
            linkedElement = new PitchElement(this);
        else if(PitchElement.class.equals(linkedElement.getClass()))
            linkedElement = new TickElement(this);
        else if(TickElement.class.equals(linkedElement.getClass()))
            linkedElement = new VolumeElement(this);
        else if(VolumeElement.class.equals(linkedElement.getClass()))
            linkedElement = new ChannelElement(this);

        oldElement.replaceConnections(linkedElement);
    }
    public void changeLinkedElement(AbstractMixerElement newElement)
    {
        linkedElement = newElement;
    }

    private void tryToConnect(MixerUIElement otherElement)
    {
        float distance = PApplet.dist(otherElement.currentX, otherElement.currentY, currentX, currentY);

        if(distance < connectionDistance && otherElement != this)
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
