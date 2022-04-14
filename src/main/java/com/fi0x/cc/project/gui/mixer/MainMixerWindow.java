package com.fi0x.cc.project.gui.mixer;

import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.elements.AbstractElement;
import com.fi0x.cc.project.mixer.elements.ISignalCreator;
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
    private static final ArrayList<AbstractElement> uiElements = new ArrayList<>();
    private final ArrayList<UISignal> uiSignals = new ArrayList<>();

    private TypeSelector typeSelector;
    private ElementSettings elementSettings;
    private AbstractElement draggingElement = null;
    private AbstractElement selectedElement = null;

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

        UIConstants.DEFAULT_BACKGROUND = color(255, 0, 0);
        UIConstants.DEFAULT_TEXT = color(0);
        UIConstants.DEFAULT_STROKE = color(0, 0, 0, 0);
        UIConstants.SELECTED_STROKE_COLOR = color(0, 0, 255);

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

        for(ElementUI mixerElement : uiElements)
            mixerElement.drawConnectionLines(lineColor);

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
            for(AbstractElement e : uiElements)
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
    public void mouseClicked()
    {
        if(typeSelector != null)
        {
            typeSelector.hide();
            typeSelector = null;
        }
        if(elementSettings != null)
        {
            elementSettings.hide();
            elementSettings = null;
        }

        if(mouseButton == LEFT)
        {
            loadPixels();
            if(pixels[mouseY * width + mouseX] == backgroundColor)
            {
                if(selectedElement != null)
                    selectedElement.select(false);
                selectedElement = null;
            } else
            {
                for(AbstractElement e : uiElements)
                {
                    if(!e.isAbove(mouseX, mouseY))
                        continue;

                    selectElement(e);
                    break;
                }
            }
        } else if(mouseButton == RIGHT)
        {
            for(AbstractElement e : uiElements)
            {
                if(e.isAbove(mouseX, mouseY))
                {
                    elementSettings = new ElementSettings(this, e.currentX, e.currentY, e);
                    elementSettings.show(e.currentX, e.currentY);
                    return;
                }
            }
            
            typeSelector = new TypeSelector(this, mouseX, mouseY);
            typeSelector.show(mouseX, mouseY);
        }
    }
    @Override
    public void mouseReleased()
    {
        if(mouseButton == LEFT && draggingElement != null)
        {
            draggingElement.drop();
            draggingElement = null;
        }
    }
    @Override
    public void mouseWheel(MouseEvent event)
    {
        for(AbstractElement e : uiElements)
        {
            if(e.isAbove(mouseX, mouseY))
            {
                e.changeMainValue(-event.getCount());
                break;
            }
        }
    }
    @Override
    public void keyPressed()
    {
        if(key == DELETE)
        {
            if(selectedElement == null)
                return;

            selectedElement.removeAllConnections();
            uiElements.remove(selectedElement);
            if(selectedElement instanceof ISignalCreator)
                MixerManager.getInstance().removeBeatListener((ISignalCreator) selectedElement);
            selectedElement = null;
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
                return;
            }
        }

        if(elementSettings != null)
        {
            int success = elementSettings.clickButton(event);
            if(success == 0)
            {
                elementSettings.hide();
                elementSettings = null;
                return;
            } else if(success > 0)
                return;
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

    public void addNewElement(AbstractElement element)
    {
        uiElements.add(element);
    }
    public static ArrayList<AbstractElement> getActiveElements()
    {
        return uiElements;
    }
    public void addUISignal(UISignal signal)
    {
        uiSignals.add(signal);
    }
    public void removeUISignal(UISignal signal)
    {
        uiSignals.remove(signal);
    }

    private void selectElement(AbstractElement element)
    {
        if(selectedElement != null)
            selectedElement.select(false);

        selectedElement = element;
        selectedElement.select(true);
    }
}
