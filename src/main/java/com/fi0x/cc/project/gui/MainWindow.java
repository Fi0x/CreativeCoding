package com.fi0x.cc.project.gui;

import com.fi0x.cc.project.LoggerManager;
import controlP5.ControlEvent;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;
import processing.core.PImage;

public class MainWindow extends PApplet
{
    private PImage icon;

    private final SynthUI[] synths = new SynthUI[4];

    @Override
    public void setup()
    {
        surface.setResizable(true);
        surface.setIcon(icon);
        surface.setTitle("Computer-Grafik-Projekt");
        surface.setSize(displayWidth / 2, displayHeight / 2);
        surface.setLocation(displayWidth / 4, displayHeight / 4);
        frameRate(60);
        background(0);
        noStroke();

        initializeSynths();
    }
    @Override
    public void settings()
    {
        icon = loadImage("logo.jpg");
    }

    @Override
    public void draw()
    {
        for(SynthUI s : synths)
            s.display();
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
        for(int i = 0; i < synths.length; i++)
        {
            try
            {
                synths[i] = new SynthUI(this, i / 2 * width / 2, i % 2 * height / 2, width / 2, height / 2);
            } catch(IllegalStateException ignored)
            {
                Logger.log("Could not initialize synth-UI for synth " + i, String.valueOf(LoggerManager.Template.DEBUG_WARNING));
                return;
            }
        }
    }
}
