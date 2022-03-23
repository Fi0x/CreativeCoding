package com.fi0x.cc.project.gui;

import com.fi0x.cc.project.synth.synthesizers.ISynthesizer;
import processing.core.PApplet;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

public class SoundVisualizer
{
    private final PApplet parentScreen;

    private int xSize;
    private int ySize;

    public float pitchBendPercent = 0.5f;
    public final Map<Integer, Float> activeNotes = new HashMap<>();
    public int lastActivity = 1000;

    public SoundVisualizer(PApplet parent, ISynthesizer synthesizer, int w, int h)
    {
        synthesizer.linkVisualizer(this);
        parentScreen = parent;

        xSize = w;
        ySize = h;
    }

    public void display()
    {
        drawBackground();

        if(lastActivity > parentScreen.frameRate * 5)
            activeNotes.clear();
        drawNotes();
        lastActivity++;
    }
    public void updateSize(int width, int height)
    {
        xSize = width;
        ySize = height;
    }

    private void drawBackground()
    {
        int background = lastActivity < parentScreen.frameRate ? parentScreen.color(255 * pitchBendPercent, 50, 50, 50) : parentScreen.color(0, 30, 50);
        parentScreen.fill(background);
        parentScreen.stroke(0);
        parentScreen.strokeWeight(2);
        parentScreen.rect(0, 0, xSize, ySize);
        parentScreen.noStroke();
    }
    private void drawNotes()
    {
        parentScreen.strokeWeight(1);
        try
        {
            for(Map.Entry<Integer, Float> note : activeNotes.entrySet())
            {
                parentScreen.stroke(0);
                int xPos = (int) (((float) note.getKey()) / 128f * xSize);
                parentScreen.line(xPos, ySize, xPos, 0);
                parentScreen.fill(0);
                parentScreen.ellipse(xPos, parentScreen.random(ySize), 2, 2);
                //TODO: Draw something cooler (volume?)
            }
        } catch(ConcurrentModificationException ignored)
        {
        }
        parentScreen.noStroke();
    }
}
