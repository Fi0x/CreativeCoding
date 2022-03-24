package com.fi0x.cc.project.gui;

import com.fi0x.cc.project.synth.synthesizers.ISynthesizer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

public class SoundVisualizer
{
    private final PApplet parentScreen;

    private int xSize;
    private int ySize;

    private final PGraphics area;

    public float pitchBendPercent = 0.5f;
    public final Map<Integer, Float> activeNotes = new HashMap<>();
    public int lastActivity = 1000;

    public SoundVisualizer(PApplet parent, ISynthesizer synthesizer, int w, int h)
    {
        synthesizer.linkVisualizer(this);
        parentScreen = parent;

        xSize = w;
        ySize = h;

        area = parent.createGraphics(xSize, ySize);
    }

    public void display()
    {
        drawBackground();

        if(lastActivity > parentScreen.frameRate * 5)
            activeNotes.clear();
        drawNotes();
        parentScreen.image(area, 0, 0);

        lastActivity++;
    }
    public void updateSize(int width, int height)
    {
        xSize = width;
        ySize = height;

        area.setSize(width, height);
    }

    private void drawBackground()
    {
        parentScreen.fill(0, 0);
        parentScreen.stroke(0);
        parentScreen.strokeWeight(2);
        parentScreen.rect(0, 0, xSize, ySize);
        parentScreen.noStroke();
    }
    private void drawNotes()
    {
        area.beginDraw();
        int background = lastActivity < parentScreen.frameRate ? parentScreen.color(255 * pitchBendPercent, 50, 50, 50) : parentScreen.color(0, 30, 50);
        area.background(background);

        area.colorMode(PConstants.HSB);
        try
        {
            for(Map.Entry<Integer, Float> note : activeNotes.entrySet())
            {
                float volumePercent = note.getValue() / 128;
                area.stroke(volumePercent * 255, 255, 255);
                area.strokeWeight(volumePercent * 2);
                area.fill(volumePercent * 255, 255, 255);

                int xPos = (int) (((float) note.getKey()) / 128f * xSize);
                area.line(xPos, ySize, xPos, 0);
                int size = (int) (volumePercent * 5 + 2);
                area.ellipse(xPos, parentScreen.random(ySize), size, size);
            }
        } catch(ConcurrentModificationException ignored)
        {
        }
        area.noStroke();

        area.endDraw();
    }
}
