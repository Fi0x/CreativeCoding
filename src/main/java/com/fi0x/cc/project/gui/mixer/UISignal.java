package com.fi0x.cc.project.gui.mixer;

import processing.core.PApplet;
import processing.core.PVector;

public class UISignal
{
    private final PApplet parent;
    private final OldMixerUIElement source;
    private final OldMixerUIElement target;
    private final int color;

    private final int stepCount;
    private int currentStep;

    public UISignal(PApplet parentScreen, OldMixerUIElement src, OldMixerUIElement dest, int transferFrames, int baseColor)
    {
        parent = parentScreen;
        source = src;
        target = dest;

        color = parent.lerpColor(baseColor, parent.color(parent.random(255), parent.random(255), parent.random(255)), 0.3f);

        stepCount = transferFrames;
        currentStep = 0;
    }

    public void draw()
    {
        PVector vec = new PVector(source.currentX - target.currentX, source.currentY - target.currentY);
        float x = source.currentX - vec.x * currentStep / stepCount;
        float y = source.currentY - vec.y * currentStep / stepCount;

        parent.fill(color);
        parent.ellipse(x, y, 20, 20);

        currentStep++;
        if(currentStep >= stepCount)
            MainMixerWindow.removeUISignal(this);
    }
}
