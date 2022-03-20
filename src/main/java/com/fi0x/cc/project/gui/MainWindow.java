package com.fi0x.cc.project.gui;

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

        JFrame f = (JFrame) ((PSurfaceAWT.SmoothCanvas)getSurface().getNative()).getFrame();
        f.setLocation(0, 0);
        f.setExtendedState(f.getExtendedState() | Frame.MAXIMIZED_BOTH);

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
        icon = loadImage("logo.jpg");
    }

    @Override
    public void draw()
    {
        for(int i = 0; i < synths.length; i++)
        {
            synths[i].updateSize(i / 2 * width / 2, i % 2 * height / 2, width / 2, height / 2);
            synths[i].display();
        }
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
