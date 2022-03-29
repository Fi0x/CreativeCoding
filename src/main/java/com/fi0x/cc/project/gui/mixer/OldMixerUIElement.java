package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.old.*;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

@Deprecated
public class OldMixerUIElement
{
    private final MainMixerWindow parent;
    protected int currentX;
    protected int currentY;
    private final int size = 100;

    private final ArrayList<OldMixerUIElement> blacklistedElements = new ArrayList<>();
    private final ArrayList<OldMixerUIElement> whitelistedElements = new ArrayList<>();

    private int backgroundColor;
    private final int selectionColor;
    private final int textColor;
    private final int strokeColor;
    private int adjustableBackgroundColor;
    private int adjustableStrokeColor;
    private float colorReverseRate;

    private boolean isSelected = false;
    private boolean pickedUp = false;
    private final int connectionDistance = 200;

    private OldAbstractMixerElement linkedElement;

    public OldMixerUIElement(MainMixerWindow parentScreen, int xCenter, int yCenter)
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

        linkedElement = new OldChannelElement(this);
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
        parent.strokeWeight(5);
        parent.fill(adjustableBackgroundColor);
        parent.ellipse(currentX, currentY, size, size);
        parent.noStroke();

        parent.fill(textColor);
        parent.textSize(14);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text(linkedElement.getDisplayName(), currentX, currentY);

        adjustableStrokeColor = parent.lerpColor(adjustableStrokeColor, strokeColor, colorReverseRate);
        adjustableBackgroundColor = parent.lerpColor(adjustableBackgroundColor, backgroundColor, colorReverseRate);
    }
    public void drawLines(int color)
    {
        drawConnectionLines(color);
    }
    public void blinkColor(float resetRate)
    {
        colorReverseRate = resetRate;
        adjustableBackgroundColor = parent.color(255);
    }
    public void blinkStroke(float resetRate)
    {
        colorReverseRate = resetRate;

        if(linkedElement instanceof ISignalSenderElement)
            adjustableStrokeColor = parent.color(0, 255, 0);
        else if(linkedElement instanceof IParameterElement)
            adjustableStrokeColor = parent.color(0, 0, 255);
        else if(linkedElement instanceof INumberElement)
            adjustableStrokeColor = parent.color(0, 255, 0);
        else
            adjustableStrokeColor = parent.color(0, 255, 0);
    }
    public void sendPulse(OldMixerUIElement target, float travelTime)
    {
        int transferFrames = (int) (travelTime * parent.frameRate);
        parent.addUISignal(new UISignal(parent, this, target, transferFrames, adjustableBackgroundColor));
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
        for(OldMixerUIElement e : MainMixerWindow.oldUiElements)
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

    public void addElementToBlacklist(OldMixerUIElement element)
    {
        whitelistedElements.remove(element);
        blacklistedElements.add(element);

        linkedElement.removeConnectedElement(element.linkedElement);
    }
    public void addElementToWhitelist(OldMixerUIElement element)
    {
        blacklistedElements.remove(element);
        whitelistedElements.add(element);

        tryToConnect(element);
    }

    public OldAbstractMixerElement getLinkedElement()
    {
        return linkedElement;
    }
    public void selectNextElement()
    {
        OldAbstractMixerElement oldElement = linkedElement;

        if(OldChannelElement.class.equals(linkedElement.getClass()))
            linkedElement = new OldDelayElement(this);
        else if(OldDelayElement.class.equals(linkedElement.getClass()))
            linkedElement = new OldIncreasingElement(this);
        else if(OldIncreasingElement.class.equals(linkedElement.getClass()))
            linkedElement = new OldIntervalElement(this);
        else if(OldIntervalElement.class.equals(linkedElement.getClass()))
            linkedElement = new OldLengthElement(this);
        else if(OldLengthElement.class.equals(linkedElement.getClass()))
            linkedElement = new OldNoteElement(this);
        else if(OldNoteElement.class.equals(linkedElement.getClass()))
            linkedElement = new OldPitchElement(this);
        else if(OldPitchElement.class.equals(linkedElement.getClass()))
            linkedElement = new OldTickElement(this);
        else if(OldTickElement.class.equals(linkedElement.getClass()))
            linkedElement = new OldVolumeElement(this);
        else if(OldVolumeElement.class.equals(linkedElement.getClass()))
            linkedElement = new OldChannelElement(this);

        oldElement.replaceConnections(linkedElement);

        if(linkedElement instanceof ISignalSenderElement)
        {
            backgroundColor = parent.color(0, 0, 255);
            adjustableBackgroundColor = backgroundColor;
        } else if(linkedElement instanceof IParameterElement)
        {
            backgroundColor = parent.color(255, 255, 0);
            adjustableBackgroundColor = backgroundColor;
        } else if(linkedElement instanceof INumberElement)
        {
            backgroundColor = parent.color(182, 0, 207);
            adjustableBackgroundColor = backgroundColor;
        } else
        {
            backgroundColor = parent.color(255, 0, 0);
            adjustableBackgroundColor = backgroundColor;
        }
    }
    public void changeLinkedElement(OldAbstractMixerElement newElement)
    {
        linkedElement = newElement;
        if(linkedElement instanceof ISignalSenderElement)
        {
            backgroundColor = parent.color(0, 0, 255);
            adjustableBackgroundColor = backgroundColor;
        } else if(linkedElement instanceof OldChannelElement)
        {
            backgroundColor = parent.color(255, 0, 0);
            adjustableBackgroundColor = backgroundColor;
        } else
        {
            backgroundColor = parent.color(255, 255, 0);
            adjustableBackgroundColor = backgroundColor;
        }
    }

    private void tryToConnect(OldMixerUIElement otherElement)
    {
        if(blacklistedElements.contains(otherElement))
            return;
        if(!linkedElement.canConnectTo(otherElement.linkedElement))
            return;

        float distance = PApplet.dist(otherElement.currentX, otherElement.currentY, currentX, currentY);
        if(whitelistedElements.contains(otherElement) || (distance < connectionDistance && otherElement != this))
        {
            otherElement.getLinkedElement().addConnectedElement(linkedElement);
            linkedElement.addConnectedElement(otherElement.getLinkedElement());
        }
    }
    private void drawConnectionLines(int color)
    {
        parent.stroke(color);
        parent.strokeWeight(5);

        for(OldMixerUIElement e : MainMixerWindow.oldUiElements)
            drawLine(e);

        parent.noStroke();
    }
    private void drawLine(OldMixerUIElement otherUI)
    {
        if(blacklistedElements.contains(otherUI))
            return;
        if(!linkedElement.canConnectTo(otherUI.linkedElement))
            return;

        float dist = PApplet.dist(currentX, currentY, otherUI.currentX, otherUI.currentY);
        if(whitelistedElements.contains(otherUI) || dist < connectionDistance)
            parent.line(currentX, currentY, otherUI.currentX, otherUI.currentY);
    }
}
