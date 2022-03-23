package com.fi0x.cc.project.gui;

import com.fi0x.cc.project.LoggerManager;
import com.fi0x.cc.project.synth.SynthManager;
import com.fi0x.cc.project.synth.synthesizers.AbstractSynth;
import com.fi0x.cc.project.synth.synthesizers.ISynthesizer;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.DropdownList;
import io.fi0x.javalogger.logging.LogEntry;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SynthUI
{
    private final ISynthesizer linkedSynth;
    private final SoundVisualizer visualizer;

    private int x;
    private int y;
    private int xSize;
    private int ySize;

    private boolean allowDropdownList = false;
    private boolean allowInstrumentChanges = true;

    private final PApplet parentScreen;
    private final ControlP5 control;

    private boolean playStatus;
    private boolean muteStatus;

    public SynthUI(PApplet parent, int xPos, int yPos, int w, int h) throws IllegalStateException
    {
        linkedSynth = SynthManager.getNextSynth();
        if(linkedSynth == null)
            throw new IllegalStateException("No Synths loaded that could be linked.");

        linkedSynth.linkUI(this);
        visualizer = new SoundVisualizer(parent, linkedSynth, w / 2, h / 2);

        if(((AbstractSynth) linkedSynth).channelNumber == 9)
        {
            allowInstrumentChanges = false;
            allowDropdownList = false;
        }

        x = xPos;
        y = yPos;
        xSize = w;
        ySize = h;

        parentScreen = parent;
        control = new ControlP5(parentScreen);
        addButtons();
    }

    public void display()
    {
        parentScreen.translate(x, y);

        parentScreen.fill(100);
        parentScreen.rect(5, 5, xSize - 10, ySize - 10);
        parentScreen.textSize(14);
        parentScreen.fill(0);
        parentScreen.text("Channel: " + ((AbstractSynth) linkedSynth).channelNumber, 10, 45, 100, 30);

        parentScreen.text("Playing", 10, 85, 75, 20);
        parentScreen.text("Muted", 10, 110, 75, 20);

        parentScreen.translate((float) xSize / 2 - 10, (float) ySize / 2 - 10);
        visualizer.display();
        parentScreen.translate((float) -xSize / 2 + 10, (float) -ySize / 2 + 10);

        parentScreen.translate(-x, -y);
    }
    public void updateDisplay()
    {
        parentScreen.translate(x, y);

        parentScreen.textSize(14);
        parentScreen.fill(0);
        parentScreen.text(linkedSynth.getInstrumentName(), (float) xSize / 2 - 100, 10, 200, 30);

        if(playStatus)
            parentScreen.fill(255, 0, 0);
        else
            parentScreen.fill(0);
        parentScreen.ellipse(100, 95, 20, 20);
        if(muteStatus)
            parentScreen.fill(255, 0, 0);
        else
            parentScreen.fill(0);
        parentScreen.ellipse(100, 120, 20, 20);

        parentScreen.translate(-x, -y);
    }
    public void updateSize(int posX, int posY, int width, int height)
    {
        x = posX;
        y = posY;
        xSize = width;
        ySize = height;

        visualizer.updateSize(width / 2, height / 2);

        updateButtonPositions();
    }
    public void noteTriggered(boolean turnedOn)
    {
        playStatus = turnedOn;
    }
    public void setMute(boolean isMuted)
    {
        muteStatus = isMuted;
    }

    public void buttonClicked(ControlEvent event)
    {
        if(event.getController() == control.getController("Previous Synth"))
            linkedSynth.previousInstrument();
        if(event.getController() == control.getController("Next Synth"))
            linkedSynth.nextInstrument();
        if(event.getController() == control.getController("DDL"))
            linkedSynth.setInstrument(SynthManager.getAllInstrumentNames()[(int) event.getValue()]);
        if(event.getController() == control.getController("Open Orca"))
        {
            try
            {
                new ProcessBuilder("E:\\Users\\Fi0x\\Documents\\Programmieren\\ORCA\\Orca Tool\\Orca.exe").start();
            } catch(IOException e)
            {
                Logger.log(new LogEntry("Could not open ORCA in Windows location, trying Linux instead", String.valueOf(LoggerManager.Template.DEBUG_WARNING)).EXCEPTION(e));
                try
                {
                    Runtime.getRuntime().exec("/home/fi0x/Documents/ORCA/Orca");
                } catch(IOException e1)
                {
                    Logger.log(new LogEntry("Could not open ORCA in Linux location, trying browser instead", String.valueOf(LoggerManager.Template.DEBUG_WARNING)).EXCEPTION(e1));
                    try
                    {
                        Desktop.getDesktop().browse(new URI("https://hundredrabbits.github.io/Orca/"));
                    } catch(IOException | URISyntaxException e2)
                    {
                        Logger.log(new LogEntry("Could not open ORCA", String.valueOf(LoggerManager.Template.DEBUG_WARNING)).EXCEPTION(e2));
                    }
                }
            }
        }
    }

    private void addButtons()
    {
        if(allowInstrumentChanges)
        {
            control.addButton("Previous Synth")
                    .setSize(100, 30)
                    .setValue(0);
            control.addButton("Next Synth")
                    .setSize(100, 30)
                    .setValue(0);
        }

        if(allowDropdownList)
        {
            control.addDropdownList("DDL")
                    .setSize(200, ySize - 10 - 10 - 35)
                    .setValue(0)
                    .setItemHeight(30)
                    .setBarHeight(30)
                    .setCaptionLabel("Select an instrument")
                    .addItems(SynthManager.getAllInstrumentNames());
        }

        if(x + y == 0)
            control.addButton("Open Orca")
                    .setSize(100, 30)
                    .setValue(0)
                    .setColorBackground(parentScreen.color(200, 0, 0));

        updateButtonPositions();
    }
    private void updateButtonPositions()
    {
        try
        {
            if(allowInstrumentChanges)
            {
                control.getController("Previous Synth")
                        .setPosition(x + 10, y + 10);
                control.getController("Next Synth")
                        .setPosition(x + xSize - 100 - 10, y + 10);
            }

            if(allowDropdownList)
            {
                DropdownList ddl = (DropdownList) control.getController("DDL")
                        .setPosition(x + (float) xSize / 2 - 100, y + 10 + 35);
                ddl.setSize(200, ySize - 10 - 10 - 35);
            }

            if(x + y == 0)
            {
                control.getController("Open Orca")
                        .setPosition(x + 10, y + ySize - 30 - 10);
            }
        } catch(Exception ignored)
        {
        }
    }
}
