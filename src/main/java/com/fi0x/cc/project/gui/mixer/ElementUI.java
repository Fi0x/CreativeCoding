package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.elements.AbstractElement;
import com.fi0x.cc.project.mixer.elements.ISignalCreator;
import com.fi0x.cc.project.mixer.elements.ISignalModifier;
import com.fi0x.cc.project.mixer.elements.Output;
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
    private ElementSettings settings = null;

    private int currentBackgroundColor;
    private int currentStrokeColor;
    private float colorReverseRate;
    private int currentHueShift;
    private int hueOffset;
    private boolean shouldUseHSB;
    private boolean reverseColor;

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

        int bgc = currentBackgroundColor;
        if(shouldUseHSB)
        {
            parent.colorMode(PConstants.HSB, 1000);
            bgc = parent.color(currentHueShift + hueOffset, 1000, 1000);
            parent.colorMode(PConstants.RGB, 255);
            if(reverseColor)
            {
                currentHueShift--;
                if(currentHueShift <= 0)
                    reverseColor = false;
            }
            else
            {
                currentHueShift ++;
                if(currentHueShift >= 200)
                    reverseColor = true;
            }
        }
        parent.fill(bgc);
        parent.ellipse(currentX, currentY, UIConstants.ELEMENT_SIZE, UIConstants.ELEMENT_SIZE);
        parent.noStroke();

        parent.fill(UIConstants.DEFAULT_TEXT);
        parent.textSize(14);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text(((AbstractElement) this).getDisplayString(), currentX, currentY);

        currentStrokeColor = parent.lerpColor(currentStrokeColor, UIConstants.DEFAULT_STROKE, colorReverseRate);
        currentBackgroundColor = parent.lerpColor(currentBackgroundColor, UIConstants.DEFAULT_BACKGROUND, colorReverseRate);
    }
    public void drawConnectionLines(int color)
    {
        parent.stroke(color);
        parent.strokeWeight(5);

        drawOutputLine();

        parent.noStroke();
    }

    public void openMenu(ElementSettings settingsElement)
    {
        settings = settingsElement;
    }
    public void closeMenu()
    {
        settings = null;
    }
    public void updateValue(int increment)
    {
        if(settings == null)
            ((AbstractElement) this).changeMainValue(increment);
        else
        {
            //TODO: Check which setting to update and change correct value
        }
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
        if(settings != null)
            return false;

        float distance = PApplet.dist(parent.mouseX, parent.mouseY, currentX, currentY);
        if(distance > UIConstants.ELEMENT_SIZE / 2f)
            return false;

        pickedUp = true;
        ((AbstractElement) this).removeAllConnections();

        return true;
    }
    public void drop()
    {
        updateConnections();
        pickedUp = false;
    }
    public void updateConnections()
    {
        if(this instanceof Output)
            return;

        Output closestOutput = (Output) getClosestNode(null);
        if(closestOutput != null)
        {
            AbstractElement closestNode = (AbstractElement) getClosestNode(closestOutput);
            ((AbstractElement) this).connectToNode(closestNode);
        }
    }
    public void sendPulse(ElementUI target, float beatTravelFrames)
    {
        if(target == null)
            return;

        int transferFrames = (int) (beatTravelFrames * parent.frameRate);
        PVector src = new PVector(currentX, currentY);
        PVector dst = new PVector(target.currentX, target.currentY);
        parent.addUISignal(new UISignal(parent, src, dst, transferFrames));
    }
    public void noteUpdate(int blinkColor, float resetRate)
    {
        colorReverseRate = resetRate;
        currentBackgroundColor = blinkColor;
        currentStrokeColor = blinkColor;

        //TODO: Create pulse animation on element size
    }
    public void startColorAnimation()
    {
        shouldUseHSB = true;
        hueOffset = this instanceof ISignalModifier ? 700 : 200;
    }

    private ElementUI getClosestNode(Output desiredOutput)
    {
        ElementUI closest = null;
        float currentDist = 0;
        for(AbstractElement e : MainMixerWindow.getActiveElements())
        {
            if(e == this || e instanceof ISignalCreator || isLoop(e))
                continue;

            if(e instanceof Output || (desiredOutput != null && e.getFinalOutput() == desiredOutput))
            {
                if(!isInAngle(desiredOutput, e))
                    continue;

                float dist = PApplet.dist(currentX, currentY, e.currentX, e.currentY);
                if(dist < currentDist || currentDist == 0)
                {
                    closest = e;
                    currentDist = dist;
                }
            }
        }
        return closest;
    }
    private boolean isLoop(AbstractElement otherElement)
    {
        AbstractElement nextHop = otherElement;
        while(nextHop != null)
        {
            if(nextHop == this)
                return true;

            nextHop = nextHop.getConnectedNode();
        }
        return false;
    }
    private boolean isInAngle(ElementUI outputElement, ElementUI nextNodeElement)
    {
        if(outputElement == null || nextNodeElement instanceof Output)
            return true;

        PVector relativeOutNode = new PVector(currentX - outputElement.currentX, currentY - outputElement.currentY);
        PVector relativeNextNode = new PVector(currentX - nextNodeElement.currentX, currentY - nextNodeElement.currentY);

        float degrees = PApplet.degrees(PVector.angleBetween(relativeOutNode, relativeNextNode));
        return degrees < UIConstants.MAX_CONNECTION_ANGLE;
    }
    private void drawOutputLine()
    {
        AbstractElement nextNode = ((AbstractElement) this).getConnectedNode();
        if(nextNode == null)
            return;

        parent.line(currentX, currentY, nextNode.currentX, nextNode.currentY);
    }
}
