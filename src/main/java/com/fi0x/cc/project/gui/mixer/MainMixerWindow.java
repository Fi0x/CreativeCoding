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
import java.util.ConcurrentModificationException;

public class MainMixerWindow extends PApplet
{
    private PImage icon;
    private final int backgroundColor = color(0);
    private final int lineColor = color(255);

    private Thread handler;
    public static MixerUIElement originElement;
    public static final ArrayList<MixerUIElement> uiElements = new ArrayList<>();
    private static final ArrayList<UISignal> uiSignals = new ArrayList<>();

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

        JFrame f = (JFrame) ((PSurfaceAWT.SmoothCanvas) getSurface().getNative()).getFrame();
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

        originElement.drawLines(lineColor);
        for(MixerUIElement mixerElement : uiElements)
            mixerElement.drawLines(lineColor);

        try
        {
            for(UISignal signal : uiSignals)
                signal.draw();
        } catch(ConcurrentModificationException ignored)
        {
        }

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
            loadPixels();
            if(pixels[mouseY * width + mouseX] == lineColor)
            {
                for(MixerUIElement e : uiElements)
                {
                    for(MixerUIElement e1 : e.getLinkedElement().getConnectedUIs())
                    {
                        float d1 = dist(mouseX, mouseY, e.currentX, e.currentY);
                        float d2 = dist(mouseX, mouseY, e1.currentX, e1.currentY);
                        float d3 = dist(e.currentX, e.currentY, e1.currentX, e1.currentY);
                        if(d1 + d2 < d3 + 1 && d1 + d2 > d3 - 1)
                        {
                            e.addElementToBlacklist(e1);
                            e1.addElementToBlacklist(e);
                            return;
                        }
                    }
                }
            }

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
            if(selectedElement == null || selectedElement == originElement)
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
        } else if(key == CODED && keyCode == UP)
        {
            if(originElement.isAbove())
                originElement.getLinkedElement().changeSecondaryValue(1);
            else
            {
                for(MixerUIElement e : uiElements)
                {
                    if(e.isAbove())
                    {
                        e.getLinkedElement().changeSecondaryValue(1);
                        break;
                    }
                }
            }
        } else if(key == CODED && keyCode == DOWN)
        {
            if(originElement.isAbove())
                originElement.getLinkedElement().changeSecondaryValue(-1);
            else
            {
                for(MixerUIElement e : uiElements)
                {
                    if(e.isAbove())
                    {
                        e.getLinkedElement().changeSecondaryValue(-1);
                        break;
                    }
                }
            }
        } else if(key == 'C' || key == 'c')
        {
            if(selectedElement != null)
            {
                if(originElement.isAbove())
                {
                    originElement.addElementToWhitelist(selectedElement);
                    selectedElement.addElementToWhitelist(originElement);
                    return;
                }
                for(MixerUIElement e : uiElements)
                {
                    if(e.isAbove())
                    {
                        e.addElementToWhitelist(selectedElement);
                        selectedElement.addElementToWhitelist(e);
                        break;
                    }
                }
            }
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

    public static void addUISignal(UISignal signal)
    {
        uiSignals.add(signal);
    }
    public static void removeUISignal(UISignal signal)
    {
        uiSignals.remove(signal);
    }

    private void selectElement(MixerUIElement element)
    {
        if(selectedElement != null)
            selectedElement.select(false);

        selectedElement = element;
        selectedElement.select(true);
    }
}
