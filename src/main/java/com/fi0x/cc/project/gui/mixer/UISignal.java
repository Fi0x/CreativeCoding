package com.fi0x.cc.project.gui.mixer;

import processing.core.PVector;

import java.util.Vector;

public class UISignal
{
    private final MainMixerWindow parent;
    private final PVector source;
    private final PVector target;
    private final int color;

    private final int stepCount;
    private int currentStep;

    public UISignal(MainMixerWindow parentScreen, PVector src, PVector dest, int transferFrames, int baseColor)
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
        PVector vec = new PVector(source.x - target.x, source.y - target.y);
        float x = source.x - vec.x * currentStep / stepCount;
        float y = source.y - vec.y * currentStep / stepCount;

        parent.fill(color);
        parent.ellipse(x, y, 20, 20);

        currentStep++;
        if(currentStep >= stepCount)
            parent.removeUISignal(this);
    }
}