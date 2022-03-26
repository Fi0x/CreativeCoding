package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.*;
import com.fi0x.cc.project.mixer.elements.*;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

public class MixerUIElement
{
    private final PApplet parent;
    protected int currentX;
    protected int currentY;
    private final int size = 100;

    private final ArrayList<MixerUIElement> blacklistedElements = new ArrayList<>();
    private final ArrayList<MixerUIElement> whitelistedElements = new ArrayList<>();

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
        parent.strokeWeight(5);
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
    public void sendPulse(MixerUIElement target, float travelTime)
    {
        int transferFrames = (int) (travelTime * parent.frameRate);
        MainMixerWindow.addUISignal(new UISignal(parent, this, target, transferFrames, adjustableBackgroundColor));
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

    public void addElementToBlacklist(MixerUIElement element)
    {
        whitelistedElements.remove(element);
        blacklistedElements.add(element);

        linkedElement.removeConnectedElement(element.linkedElement);
    }
    public void addElementToWhitelist(MixerUIElement element)
    {
        blacklistedElements.remove(element);
        whitelistedElements.add(element);

        tryToConnect(element);
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
            linkedElement = new IncreasingElement(this);
        else if(IncreasingElement.class.equals(linkedElement.getClass()))
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
    public void changeLinkedElement(AbstractMixerElement newElement)
    {
        linkedElement = newElement;
        if(linkedElement instanceof ISignalSenderElement)
        {
            backgroundColor = parent.color(0, 0, 255);
            adjustableBackgroundColor = backgroundColor;
        } else if(linkedElement instanceof ChannelElement)
        {
            backgroundColor = parent.color(255, 0, 0);
            adjustableBackgroundColor = backgroundColor;
        } else
        {
            backgroundColor = parent.color(255, 255, 0);
            adjustableBackgroundColor = backgroundColor;
        }
    }

    private void tryToConnect(MixerUIElement otherElement)
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

        drawLine(MainMixerWindow.originElement);
        for(MixerUIElement e : MainMixerWindow.uiElements)
            drawLine(e);

        parent.noStroke();
    }
    private void drawLine(MixerUIElement otherUI)
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
