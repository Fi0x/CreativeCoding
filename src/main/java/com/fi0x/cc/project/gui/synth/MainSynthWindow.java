package com.fi0x.cc.project.gui.synth;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.synth.UDP.UDPProcessor;
import com.fi0x.cc.project.synth.midi.MidiHandler;
import controlP5.ControlEvent;
import io.fi0x.javalogger.logging.Logger;
import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PImage;

import javax.swing.*;
import java.awt.*;

public class MainSynthWindow extends PApplet
{
    private PImage icon;

    private final int xSynths = 4;
    private final int ySynths = 4;
    private final SynthUI[] synths = new SynthUI[xSynths * ySynths];

    private boolean reDrawNeeded = true;

    @Override
    public void setup()
    {
        surface.setResizable(false);
        surface.setIcon(icon);
        surface.setTitle("Computer-Grafik-Projekt ~Main~");
        surface.setSize(displayWidth / 4 * 3, displayHeight / 4 * 3);
        surface.setLocation(displayWidth / 8, displayHeight / 8);

        frameRate(60);
        background(0);
        noStroke();

        initializeSynths();
        new MidiHandler();
        new Thread(() -> UDPProcessor.getInstance().run()).start();
    }
    @Override
    public void settings()
    {
        icon = loadImage("images/logo.jpg");
    }

    @Override
    public void draw()
    {
        for(int x = 0; x < xSynths; x++)
        {
            for(int y = 0; y < ySynths; y++)
            {
                synths[x * ySynths + y].updateSize(x * width / xSynths, y * height / ySynths, width / xSynths, height / ySynths);

                if(reDrawNeeded)
                    synths[x * ySynths + y].display();

                synths[x * ySynths + y].updateDisplay();
            }
        }
        reDrawNeeded = false;
    }

    @Override
    public void frameResized(int w, int h)
    {
        super.frameResized(w, h);
        reDrawNeeded = true;
    }
    public void controlEvent(ControlEvent event)
    {
        if(!event.isController())
            return;

        for(SynthUI s : synths)
        {
            if(s == null)
                continue;
            s.buttonClicked(event);
        }
    }

    private void initializeSynths()
    {
        for(int x = 0; x < xSynths; x++)
        {
            for(int y = 0; y < ySynths; y++)
            {
                try
                {
                    synths[x * ySynths + y] = new SynthUI(this, x * width / xSynths, y * height / ySynths, width / xSynths, height / ySynths);
                } catch(IllegalStateException ignored)
                {
                    Logger.log("Could not initialize synth-UI for synth " + x + ", " + y, String.valueOf(LoggerManager.Template.DEBUG_WARNING));
                    return;
                }
            }
        }
    }
}
