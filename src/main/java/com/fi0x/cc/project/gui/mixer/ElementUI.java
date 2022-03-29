package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.elements.AbstractElement;

public class ElementUI
{
    private final AbstractElement linkedElement;

    private final MainMixerWindow parent;
    private int currentX;
    private int currentY;
    private boolean isHidden;

    public ElementUI(MainMixerWindow parentScreen, AbstractElement linkedLogicElement, int x, int y)
    {
        parent = parentScreen;

        linkedElement = linkedLogicElement;

        currentX = x;
        currentY = y;
        isHidden = false;
    }

    public void draw()
    {
        if(isHidden)
            return;

        parent.fill(255, 0, 0);
        parent.ellipse(currentX, currentY, 100, 100);
    }

    public void hide()
    {
        isHidden = true;
    }
    public void show()
    {
        isHidden = false;
    }
}
