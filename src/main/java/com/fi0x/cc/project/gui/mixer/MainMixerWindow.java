package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.TimerElement;
import controlP5.ControlEvent;
import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PSurface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainMixerWindow extends PApplet
{
    private PImage icon;

    private Thread handler;
    public static final ArrayList<AbstractMixerUIElement> uiElements = new ArrayList<>();

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

        handler = new Thread(MixerManager.getInstance());
        handler.start();
    }
    @Override
    public void settings()
    {
        icon = loadImage("images/logo.jpg");
    }
    @Override
    public void draw()
    {
        for(AbstractMixerUIElement mixerElement : uiElements)
            mixerElement.draw();
    }

    @Override
    public void mouseClicked()
    {
        uiElements.add(new TimerUIElement(this, mouseX, mouseY, new TimerElement()));
        //TODO: Create new element or pick up one if mouse is over one
    }
    @Override
    public void exit()
    {
        handler.interrupt();
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
        //TODO: handle user-input
    }
}
