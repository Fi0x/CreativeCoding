package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.AbstractMixerElement;
import controlP5.ControlEvent;
import processing.core.PApplet;

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
    }
    public void controlChangedEvent(ControlEvent event)
    {
        //TODO: Handle button presses
    }

    public boolean pickUp()
    {
        float distance = PApplet.dist(parent.mouseX, parent.mouseY, currentX, currentY);
        if(distance > (float) size / 2)
            return false;

        pickedUp = true;
        return true;
    }
    public void drop()
    {
        pickedUp = false;
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
