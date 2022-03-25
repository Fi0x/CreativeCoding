package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.AbstractMixerElement;
import processing.core.PApplet;

public abstract class AbstractMixerUIElement
{
    protected final PApplet parent;
    protected int currentX;
    protected int currentY;
    protected final int size = 100;

    protected AbstractMixerElement linkedElement;

    public AbstractMixerUIElement(PApplet parentApplet, int xCenter, int yCenter)
    {
        parent = parentApplet;
        currentX = xCenter;
        currentY = yCenter;
    }

    public abstract void init();
    public void draw()
    {
        parent.fill(255, 0, 0);
        parent.ellipse(currentX, currentY, size, size);
    }

    public AbstractMixerElement getLinkedElement()
    {
        return linkedElement;
    }
}
