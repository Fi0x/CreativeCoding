package com.fi0x.cc.project.gui.synth;

import com.fi0x.cc.Main;
import com.fi0x.cc.project.synth.SynthManager;
import com.fi0x.cc.project.udp.UDPProcessor;
import com.fi0x.cc.project.midi.MidiHandler;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.DropdownList;
import io.fi0x.javalogger.logging.LogEntry;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;
import processing.core.PImage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainSynthWindow extends PApplet
{
    private PImage icon;
    private ControlP5 control;
    private String[] midiEntries;

    private final int xSynths = 4;
    private final int ySynths = 4;
    private final SynthUI[] synths = new SynthUI[xSynths * ySynths];

    private int initialized = 0;

    @Override
    public void setup()
    {
        surface.setResizable(true);
        surface.setIcon(icon);
        surface.setTitle("Computer-Grafik-Projekt ~Main~");
        surface.setSize(displayWidth / 4 * 3, displayHeight / 4 * 3);
        surface.setLocation(displayWidth / 8, displayHeight / 8);

        frameRate(60);
        background(0);
        noStroke();

        control = new ControlP5(this);

        initializeSynths();

        new MidiHandler().initialize();

        fillControlBar();

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
        background(0);

        for(int x = 0; x < xSynths; x++)
        {
            for(int y = 0; y < ySynths; y++)
            {
                synths[x * ySynths + y].updateSize(x * width / xSynths, y * (height - 40) / ySynths, width / xSynths, (height - 40) / ySynths);

                synths[x * ySynths + y].display();
                synths[x * ySynths + y].updateDisplay();
            }
        }

        adjustControlBarPositions();

        if(initialized < 100)
        {
            surface.setSize(displayWidth / 4 * 3, displayHeight / 4 * 3);
            initialized++;
        }
    }

    public void controlEvent(ControlEvent event)
    {
        if(!event.isController() || initialized < 100)
            return;

        if(event.getController() == control.getController("Open Orca"))
        {
            try
            {
                new ProcessBuilder("E:\\Users\\Fi0x\\Documents\\Programmieren\\ORCA\\Orca Tool\\Orca.exe").start();
            } catch(IOException e)
            {
                Logger.log(new LogEntry("Could not open ORCA in Windows location, trying Linux instead", String.valueOf(Main.Template.DEBUG_WARNING)).EXCEPTION(e));
                try
                {
                    Runtime.getRuntime().exec("/home/fi0x/Documents/ORCA/Orca");
                } catch(IOException e1)
                {
                    Logger.log(new LogEntry("Could not open ORCA in Linux location, trying browser instead", String.valueOf(Main.Template.DEBUG_WARNING)).EXCEPTION(e1));
                    try
                    {
                        Desktop.getDesktop().browse(new URI("https://hundredrabbits.github.io/Orca/"));
                    } catch(IOException | URISyntaxException e2)
                    {
                        Logger.log(new LogEntry("Could not open ORCA", String.valueOf(Main.Template.DEBUG_WARNING)).EXCEPTION(e2));
                    }
                }
            }
            return;
        }
        if(event.getController() == control.getController("Open Mixer"))
        {
            PApplet.main("com.fi0x.cc.project.gui.mixer.MainMixerWindow");
            return;
        }
        if(event.getController() == control.getController("Midi Device"))
        {
            SynthManager.setAcceptedMidi(midiEntries[(int) event.getValue()]);
            return;
        }

        for(SynthUI s : synths)
        {
            if(s == null)
                continue;
            if(s.buttonClicked(event))
                return;
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
                    synths[x * ySynths + y] = new SynthUI(this, x * width / xSynths, y * (height - 40) / ySynths, width / xSynths, (height - 40) / ySynths);
                } catch(IllegalStateException ignored)
                {
                    Logger.log("Could not initialize synth-UI for synth " + x + ", " + y, String.valueOf(Main.Template.DEBUG_WARNING));
                    return;
                }
            }
        }
    }
    private void fillControlBar()
    {
        control.addButton("Open Orca")
                .setSize(100, 30)
                .setValue(0)
                .setColorBackground(color(200, 0, 0));
        control.addButton("Open Mixer")
                .setSize(100, 30)
                .setValue(0)
                .setColorBackground(color(200, 0, 0));

        midiEntries = MidiHandler.getDeviceNames();
        control.addDropdownList("Midi Device")
                .setSize(200, height / 2)
                .setValue(0)
                .setItemHeight(30)
                .setBarHeight(30)
                .setCaptionLabel("Select a Midi Device")
                .addItems(MidiHandler.getDeviceNames())
                .setDirection(40);

        adjustControlBarPositions();
    }
    private void adjustControlBarPositions()
    {
        control.getController("Open Orca")
                .setPosition(5, height - 35);
        control.getController("Open Mixer")
                .setPosition(110, height - 35);

        DropdownList ddl = (DropdownList) control.getController("Midi Device");
        if(ddl.isOpen())
            ddl.setPosition(215, height - 35 - midiEntries.length * ddl.getBarHeight());
        else
            ddl.setPosition(215, height - 35);
        ddl.setSize(200, height / 2);
    }
}
