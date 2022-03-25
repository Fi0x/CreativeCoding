package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.AbstractMixerElement;
import processing.core.PApplet;
import processing.core.PConstants;

public class MixerUIElement
{
    private final PApplet parent;
    protected int currentX;
    protected int currentY;
    private final int size = 100;

    private boolean pickedUp = false;
    private final int connectionDistance = 300;

    private AbstractMixerElement linkedElement;

    public MixerUIElement(PApplet parentScreen, int xCenter, int yCenter, AbstractMixerElement element)
    {
        parent = parentScreen;

        currentX = xCenter;
        currentY = yCenter;

        linkedElement = element;
    }

    public void init()
    {
    }
    public void draw()
    {
        if(pickedUp)
        {
            currentX = parent.mouseX;
            currentY = parent.mouseY;
        }

        drawConnectionLines();

        parent.fill(255, 0, 0);
        parent.ellipse(currentX, currentY, size, size);

        parent.fill(0);
        parent.textSize(20);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text(linkedElement.getDisplayName(), currentX, currentY);
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
    public void updateLocation(int newX, int newY)
    {
        currentX = newX;
        currentY = newY;
    }

    public AbstractMixerElement getLinkedElement()
    {
        return linkedElement;
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
