package com.fi0x.cc.project.gui;

import com.fi0x.cc.project.synth.synthesizers.ISynthesizer;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

public class SoundVisualizer2 extends AbstractSoundVisualizer
{
    private ArrayList<Map<Integer, Integer>> noteHistory = new ArrayList<>();

    public SoundVisualizer2(PApplet parent, ISynthesizer synthesizer, int w, int h)
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
        noteHistory.add(new HashMap<>(activeNotes));

        if(noteHistory.size() > ySize)
            noteHistory.remove(0);

        area.beginDraw();
        int background = lastActivity < parentScreen.frameRate ? parentScreen.color(255 * pitchBendPercent, 50, 50, 50) : parentScreen.color(0, 30, 50);
        area.background(background);

        area.colorMode(PConstants.HSB);

        try
        {
            for(int y = 0; y < noteHistory.size(); y++)
            {
                for(Map.Entry<Integer, Integer> note : noteHistory.get(y).entrySet())
                {
                    float volumePercent = (float) note.getValue() / 128;
                    area.stroke(volumePercent * 255, 255, 255);
                    area.strokeWeight(volumePercent * 2);
                    area.fill(volumePercent * 255, 255, 255);

                    int xPos = (int) (((float) note.getKey()) / 128f * xSize);
                    area.ellipse(xPos, y, 1, 1);
                }
            }
        } catch(ConcurrentModificationException ignored)
        {
        }

        area.endDraw();
    }
}
