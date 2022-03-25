package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.TimerElement;
import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PSurface;
import processing.event.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainMixerWindow extends PApplet
{
    private PImage icon;
    private final int backgroundColor = color(0);

    private Thread handler;
    public static MixerUIElement originElement;
    public static final ArrayList<MixerUIElement> uiElements = new ArrayList<>();

    public MixerUIElement draggingElement = null;

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
        background(backgroundColor);
        noStroke();

        originElement = new MixerUIElement(this, width / 2, height / 2, new TimerElement());

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
        background(backgroundColor);

        originElement.updateLocation(width / 2, height / 2);
        originElement.draw();
        for(MixerUIElement mixerElement : uiElements)
            mixerElement.draw();
    }

    @Override
    public void mousePressed()
    {
        if(mouseButton != LEFT)
            return;

        loadPixels();
        if(pixels[mouseY * width + mouseX] == backgroundColor)
        {
            MixerUIElement newControlElement = new MixerUIElement(this, mouseX, mouseY, new TimerElement());
            newControlElement.init();
            uiElements.add(newControlElement);
        } else
        {
            for(MixerUIElement e : uiElements)
            {
                if(!e.pickUp())
                    continue;

                draggingElement = e;
                break;
            }
        }
    }
    @Override
    public void mouseReleased()
    {
        if(mouseButton != LEFT)
            return;

        if(draggingElement == null)
            return;

        draggingElement.drop();
        draggingElement = null;
    }

    @Override
    public void mouseWheel(MouseEvent event)
    {
        if(originElement.isAbove())
            originElement.getLinkedElement().changeMainValue(-event.getCount());
        else
        {
            for(MixerUIElement e : uiElements)
            {
                if(e.isAbove())
                {
                    e.getLinkedElement().changeMainValue(-event.getCount());
                    break;
                }
            }
        }
    }
    @Override
    public void keyPressed()
    {
        System.out.println(key);
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
}
