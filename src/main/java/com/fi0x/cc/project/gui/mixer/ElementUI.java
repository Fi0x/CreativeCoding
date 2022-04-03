package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.elements.AbstractElement;
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

        drawOutputLine();

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
        //TODO: Inform connected nodes: no more linked node; no more final output node
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
        //TODO: Use this when midi signals are sent
        int transferFrames = (int) (beatTravelFrames * parent.frameRate);
        PVector src = new PVector(currentX, currentY);
        PVector dst = new PVector(target.currentX, target.currentY);
        parent.addUISignal(new UISignal(parent, src, dst, transferFrames, currentBackgroundColor));
    }

    private ElementUI getClosestNode(Output desiredOutput)
    {
        ElementUI closest = null;
        float currentDist = 0;
        for(AbstractElement e : MainMixerWindow.getActiveElements())
        {
            if(e == this || isLoop(e))
                continue;

            if(e instanceof Output || (desiredOutput != null && e.getFinalOutput() == desiredOutput))
            {
                //TODO: Add check if node is within 180° angle towards output node direction
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
    private void drawOutputLine()
    {
        AbstractElement nextNode = ((AbstractElement) this).getConnectedNode();
        if(nextNode == null)
            return;

        parent.line(currentX, currentY, nextNode.currentX, nextNode.currentY);
    }
}
