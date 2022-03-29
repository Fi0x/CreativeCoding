package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.MixerManager;
import controlP5.ControlEvent;
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
    private GlobalController controller;
    @Deprecated
    public static final ArrayList<OldMixerUIElement> oldUiElements = new ArrayList<>();
    private final ArrayList<ElementUI> uiElements = new ArrayList<>();
    private final ArrayList<UISignal> uiSignals = new ArrayList<>();

    private TypeSelector typeSelector;
    @Deprecated
    private OldMixerUIElement oldDraggingElement = null;
    @Deprecated
    private OldMixerUIElement oldSelectedElement = null;
    private ElementUI draggingElement = null;
    private ElementUI selectedElement = null;

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

        controller = new GlobalController(this);

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

        controller.draw();

        for(OldMixerUIElement mixerElement : oldUiElements)
            mixerElement.drawLines(lineColor);

        try
        {
            for(UISignal signal : uiSignals)
                signal.draw();
        } catch(ConcurrentModificationException ignored)
        {
        }

        for(ElementUI element : uiElements)
            element.draw();
    }

    @Override
    public void mousePressed()
    {
        if(mouseButton == LEFT)
        {
            loadPixels();
            if(pixels[mouseY * width + mouseX] == lineColor)
            {
                for(OldMixerUIElement e : oldUiElements)
                {
                    for(OldMixerUIElement e1 : e.getLinkedElement().getConnectedUIs())
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

            //TODO: Move to new drag-drop system
            for(OldMixerUIElement e : oldUiElements)
            {
                if(!e.pickUp())
                    continue;

                oldDraggingElement = e;

                selectElement(e);
                break;
            }
        }
    }
    @Override
    public void mouseReleased()
    {
        //TODO: Move to new drag-drop system
        if(mouseButton == LEFT && oldDraggingElement != null)
        {
            oldDraggingElement.drop();
            oldDraggingElement = null;
        }
    }
    @Override
    public void mouseClicked()
    {
        if(mouseButton == LEFT)
        {
            if(typeSelector != null)
            {
                typeSelector.hide();
                typeSelector = null;
            }
            loadPixels();
            if(pixels[mouseY * width + mouseX] == backgroundColor)
            {
                if(oldSelectedElement != null)
                    oldSelectedElement.select(false);
                oldSelectedElement = null;
                return;
            }

            for(OldMixerUIElement e : oldUiElements)
            {
                if(!e.isAbove())
                    continue;

                selectElement(e);
                break;
            }
        } else if(mouseButton == RIGHT)
        {
            if(typeSelector == null)
                typeSelector = new TypeSelector(this, mouseX, mouseY);
            typeSelector.show(mouseX, mouseY);
        }
    }
    @Override
    public void mouseWheel(MouseEvent event)
    {
        for(OldMixerUIElement e : oldUiElements)
        {
            if(e.isAbove())
            {
                e.getLinkedElement().changeMainValue(-event.getCount());
                break;
            }
        }
    }
    @Override
    public void keyPressed()
    {
        if(key == DELETE)
        {
            if(oldSelectedElement == null)
                return;

            oldUiElements.remove(oldSelectedElement);
            oldSelectedElement.getLinkedElement().removeAllConnections();
            oldSelectedElement = null;
        } else if(key == ENTER)
        {
            OldMixerUIElement newControlElement = new OldMixerUIElement(this, mouseX, mouseY);
            newControlElement.init();
            oldUiElements.add(newControlElement);
            newControlElement.drop();
        } else if(key == CODED && keyCode == UP)
        {
            for(OldMixerUIElement e : oldUiElements)
            {
                if(e.isAbove())
                {
                    e.getLinkedElement().changeSecondaryValue(1);
                    break;
                }
            }
        } else if(key == CODED && keyCode == DOWN)
        {
            for(OldMixerUIElement e : oldUiElements)
            {
                if(e.isAbove())
                {
                    e.getLinkedElement().changeSecondaryValue(-1);
                    break;
                }
            }
        } else if(key == 'C' || key == 'c')
        {
            if(oldSelectedElement != null)
            {
                for(OldMixerUIElement e : oldUiElements)
                {
                    if(e.isAbove())
                    {
                        e.addElementToWhitelist(oldSelectedElement);
                        oldSelectedElement.addElementToWhitelist(e);
                        break;
                    }
                }
            }
        }
    }

    public void controlEvent(ControlEvent event)
    {
        if(!event.isController() || controller == null)
            return;

        controller.buttonClicked(event);
        if(typeSelector != null)
        {
            if(typeSelector.selectElement(event))
            {
                typeSelector.hide();
                typeSelector = null;
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

    public void addNewElement(ElementUI element)
    {
        uiElements.add(element);
    }
    public void addUISignal(UISignal signal)
    {
        uiSignals.add(signal);
    }
    public void removeUISignal(UISignal signal)
    {
        uiSignals.remove(signal);
    }

    private void selectElement(OldMixerUIElement element)
    {
        if(oldSelectedElement != null)
            oldSelectedElement.select(false);

        oldSelectedElement = element;
        oldSelectedElement.select(true);
    }
}
