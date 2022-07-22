package com.fi0x.cc.project.synth.gui;

import com.fi0x.cc.project.synth.synthesizers.ISynthesizer;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class SoundVisualizer extends AbstractSoundVisualizer
{
    public SoundVisualizer(PApplet parent, ISynthesizer synthesizer, int w, int h)
    {
        super(parent, synthesizer, w, h);
    }

    @Override
    public void display()
    {
        drawBackground();

        if(lastActivity > parentScreen.frameRate * 5)
            activeNotes.clear();
        drawNotes();
        parentScreen.image(area, 0, 0);

        lastActivity++;
    }
    private void drawNotes()
    {
        area.beginDraw();
        int background = lastActivity < parentScreen.frameRate ? parentScreen.color(255 * pitchBendPercent, 50, 50, 50) : parentScreen.color(0, 30, 50);
        area.background(background);

        area.colorMode(PConstants.HSB);
        try
        {
            for(Map.Entry<Integer, Integer> note : activeNotes.entrySet())
            {
                float volumePercent = (float) note.getValue() / 128;
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
