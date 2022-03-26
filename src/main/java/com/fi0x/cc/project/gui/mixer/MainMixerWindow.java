package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.elements.TimerElement;
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
    public MixerUIElement selectedElement = null;

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

        frameRate(120);
        background(backgroundColor);
        noStroke();

        originElement = new MixerUIElement(this, width / 2, height / 2);
        originElement.changeLinkedElement(new TimerElement(originElement));

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

        originElement.drawLines();
        for(MixerUIElement mixerElement : uiElements)
            mixerElement.drawLines();

        originElement.drawElement();
        for(MixerUIElement mixerElement : uiElements)
            mixerElement.drawElement();
    }

    @Override
    public void mousePressed()
    {
        if(mouseButton == RIGHT)
        {
            loadPixels();
            if(pixels[mouseY * width + mouseX] == backgroundColor)
                return;

            for(MixerUIElement e : uiElements)
            {
                if(!e.isAbove())
                    continue;

                e.selectNextElement();
                break;
            }
        }
        if(mouseButton == LEFT)
        {
            for(MixerUIElement e : uiElements)
            {
                if(!e.pickUp())
                    continue;

                draggingElement = e;

                selectElement(e);
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

        selectedElement.select(false);
        selectedElement = null;
    }
    @Override
    public void mouseClicked()
    {
        if(mouseButton != LEFT)
            return;

        loadPixels();
        if(pixels[mouseY * width + mouseX] == backgroundColor)
        {
            if(selectedElement != null)
                selectedElement.select(false);
            selectedElement = null;
            return;
        }

        if(originElement.isAbove())
        {
            selectElement(originElement);
            return;
        }
        for(MixerUIElement e : uiElements)
        {
            if(!e.isAbove())
                continue;

            selectElement(e);
            break;
        }
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
        if(key == DELETE)
        {
            if(selectedElement == originElement)
                return;

            uiElements.remove(selectedElement);
            selectedElement.getLinkedElement().removeAllConnections();
            selectedElement = null;
        } else if(key == ENTER)
        {
            MixerUIElement newControlElement = new MixerUIElement(this, mouseX, mouseY);
            newControlElement.init();
            uiElements.add(newControlElement);
            newControlElement.drop();
        }
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

    private void selectElement(MixerUIElement element)
    {
        if(selectedElement != null)
            selectedElement.select(false);

        selectedElement = element;
        selectedElement.select(true);
    }
}
