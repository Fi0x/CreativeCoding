package com.fi0x.cc.project.synth.gui;

import com.fi0x.cc.project.synth.synthesizers.ISynthesizer;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSoundVisualizer
{
    protected final PApplet parentScreen;

    protected int xSize;
    protected int ySize;
    protected final PGraphics area;

    public float pitchBendPercent = 0.5f;
    public final Map<Integer, Integer> activeNotes = new HashMap<>();
    public int lastActivity = 1000;

    public AbstractSoundVisualizer(PApplet parent, ISynthesizer synthesizer, int w, int h)
    {
        synthesizer.linkVisualizer(this);
        parentScreen = parent;

        xSize = w;
        ySize = h;

        area = parent.createGraphics(xSize, ySize);
    }

    public abstract void display();
    public void updateSize(int width, int height)
    {
        xSize = width;
        ySize = height;

        area.setSize(width, height);
    }

    protected void drawBackground()
    {
        parentScreen.fill(0, 0);
        parentScreen.stroke(0);
        parentScreen.strokeWeight(2);
        parentScreen.rect(0, 0, xSize, ySize);
        parentScreen.noStroke();
    }
}
