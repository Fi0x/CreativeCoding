package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.AbstractMixerElement;
import controlP5.ControlEvent;
import processing.core.PApplet;
import processing.core.PConstants;

public class MixerUIElement
{
    protected final PApplet parent;
    protected int currentX;
    protected int currentY;
    protected final int size = 100;

    private boolean pickedUp = false;

    protected AbstractMixerElement linkedElement;

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

        parent.fill(255, 0, 0);
        parent.ellipse(currentX, currentY, size, size);

        parent.fill(0);
        parent.textSize(20);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text(linkedElement.getDisplayName(), currentX, currentY);
    }

    public boolean pickUp()
    {
        float distance = PApplet.dist(parent.mouseX, parent.mouseY, currentX, currentY);
        if(distance > size / 2f)
            return false;

        pickedUp = true;
        return true;
    }
    public void drop()
    {
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
}
