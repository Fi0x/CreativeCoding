package com.fi0x.cc.project.mixer.gui;

import com.fi0x.cc.project.mixer.MixerManager;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISignalCreator;
import com.fi0x.cc.project.mixer.elements.Input;
import com.fi0x.cc.project.midi.MidiHandler;
import processing.awt.PSurfaceAWT;
import processing.core.*;
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
    private BeatController beatController;
    private static final ArrayList<AbstractElement> uiElements = new ArrayList<>();
    private final ArrayList<UISignal> uiSignals = new ArrayList<>();
    public float currentScale = 1;
    public final PVector currentTranslation = new PVector(0, 0);
    private PVector mouseDragStart;

    private TypeSelector newTypeSelector;
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
        textSize(14);
        textAlign(PConstants.CENTER, PConstants.CENTER);

        beatController = new BeatController(this);
        beatController.updateLocation(-width / 2, -height / 2);

        UIConstants.DEFAULT_ELEMENT_BACKGROUND = color(181, 25, 25);
        UIConstants.SETTINGS_ELEMENT_BACKGROUND = color(219, 141, 46);
        UIConstants.DEFAULT_TEXT = color(0);
        UIConstants.DEFAULT_STROKE = color(0, 0, 0, 0);
        UIConstants.SELECTED_STROKE_COLOR = color(18, 43, 201);

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
        translate(width / 2f, height / 2f);
        scale(currentScale);
        translate(currentTranslation.x, currentTranslation.y);

        background(backgroundColor);
        beatController.draw();

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

        if(elementSettings != null)
            elementSettings.draw();

        if(newTypeSelector != null)
            newTypeSelector.draw();
    }

    @Override
    public void mousePressed()
    {
        if(mouseButton == LEFT)
        {
            loadPixels();
            if(pixels[mouseY * width + mouseX] == backgroundColor)
                mouseDragStart = new PVector(mouseX, mouseY);
            else
            {
                for(AbstractElement e : uiElements)
                {
                    if(!e.pickUp())
                        continue;

                    if(elementSettings != null)
                    {
                        elementSettings.hide();
                        elementSettings = null;
                    }
                    draggingElement = e;

                    selectElement(e);
                    break;
                }
            }
        }
    }
    @Override
    public void mouseClicked()
    {
        if(mouseButton == LEFT)
        {
            if(elementSettings != null)
            {
                elementSettings.hide();
                elementSettings = null;
            }

            loadPixels();
            if(pixels[mouseY * width + mouseX] == backgroundColor)
            {
                if(selectedElement != null)
                    selectedElement.select(false);
                selectedElement = null;
            } else
            {
                if(newTypeSelector != null)
                {
                    if(newTypeSelector.selectElement((int) (transMouse().x), (int) (transMouse().y)))
                    {
                        newTypeSelector = null;
                        return;
                    }
                    newTypeSelector = null;
                }

                if(beatController != null)
                {
                    if(beatController.interact((int) (transMouse().x), (int) (transMouse().y)))
                        return;
                }

                for(AbstractElement e : uiElements)
                {
                    if(!e.isAbove(transMouse().x, transMouse().y))
                        continue;

                    selectElement(e);
                    break;
                }
            }
        } else if(mouseButton == RIGHT)
        {
            for(AbstractElement e : uiElements)
            {
                if(e.isAbove(transMouse().x, transMouse().y))
                {
                    boolean same = false;
                    if(elementSettings != null)
                    {
                        if(elementSettings.getLinkedElement() == e)
                            same = true;

                        elementSettings.hide();
                        elementSettings = null;
                    }

                    if(!same)
                    {
                        elementSettings = new ElementSettings(this, e.currentX, e.currentY, e);
                        elementSettings.show(e.currentX, e.currentY);
                    }
                    return;
                }
            }
            
            newTypeSelector = new TypeSelector(this, (int) (transMouse().x), (int) (transMouse().y));
        }
    }
    @Override
    public void mouseReleased()
    {
        if(mouseButton == LEFT)
        {
            if(mouseDragStart != null)
            {
                PVector dist = new PVector(mouseX, mouseY);
                dist.add(mouseDragStart.mult(-1));
                currentTranslation.add(dist);
                beatController.updateLocation((int) -dist.x, (int) -dist.y);
                mouseDragStart = null;
            }
            else if(draggingElement != null)
            {
                draggingElement.drop();
                draggingElement = null;
            }
        }
    }
    @Override
    public void mouseWheel(MouseEvent event)
    {
        for(AbstractElement e : uiElements)
        {
            if(e.isAbove(transMouse().x, transMouse().y))
            {
                e.updateValue(-event.getCount());
                return;
            }
        }

        loadPixels();
        if(pixels[mouseY * width + mouseX] == backgroundColor)
        {
            currentScale *= event.getCount() > 0 ? 0.5 : 2;
            float bCTransMulti = event.getCount() > 0 ? -0.5f : 0.5f;
            bCTransMulti /= currentScale;
            System.out.println(currentScale);
            beatController.updateLocation((int) (width * bCTransMulti), (int) (height * bCTransMulti));//FIXME
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
            if(selectedElement instanceof Input)
                MidiHandler.inputElements.remove(selectedElement);
            selectedElement = null;
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

    public PVector transMouse()
    {
        int correctMouseX = (int) ((mouseX - width / 2) / currentScale - currentTranslation.x);
        int correctMouseY = (int) ((mouseY - height / 2) / currentScale - currentTranslation.y);
        return new PVector(correctMouseX, correctMouseY);
    }
}
