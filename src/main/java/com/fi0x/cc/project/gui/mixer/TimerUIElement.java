package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.AbstractMixerElement;
import processing.core.PApplet;

public class TimerUIElement extends AbstractMixerUIElement
{
    public TimerUIElement(PApplet parentApplet, int xCenter, int yCenter, AbstractMixerElement element)
    {
        super(parentApplet, xCenter, yCenter);
        linkedElement = element;
    }

    @Override
    public void init()
    {
    }

    @Override
    public void draw()
    {
        super.draw();
    }
}
