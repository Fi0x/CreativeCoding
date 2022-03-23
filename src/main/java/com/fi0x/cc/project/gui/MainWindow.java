package com.fi0x.cc.project.gui;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.synth.UDP.UDPProcessor;
import com.fi0x.cc.project.synth.midi.MidiHandler;
import controlP5.ControlEvent;
import io.fi0x.javalogger.logging.Logger;
import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PSurface;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends PApplet
{
    private PImage icon;

    private final int xSynths = 4;
    private final int ySynths = 4;
    private final SynthUI[] synths = new SynthUI[xSynths * ySynths];

    private boolean initialization = true;

    @Override
    public void setup()
    {
        surface.setResizable(true);
        surface.setIcon(icon);
        surface.setTitle("Computer-Grafik-Projekt ~Main~");
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

                if(initialization)
                    synths[x * ySynths + y].display();

                synths[x * ySynths + y].updateDisplay();
            }
        }
    }

    @Override
    protected PSurface initSurface()
    {
        PSurface sur = super.initSurface();
        PSurfaceAWT awt = (PSurfaceAWT) surface;
        PSurfaceAWT.SmoothCanvas can = (PSurfaceAWT.SmoothCanvas) awt.getNative();
        Frame f = can.getFrame();
        f.setUndecorated(true);
        return sur;
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
