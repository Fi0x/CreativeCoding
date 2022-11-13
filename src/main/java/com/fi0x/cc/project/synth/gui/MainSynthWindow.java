package com.fi0x.cc.project.synth.gui;

import com.fi0x.cc.Main;
import com.fi0x.cc.project.synth.SynthManager;
import com.fi0x.cc.project.synth.udp.UDPProcessor;
import com.fi0x.cc.project.midi.MidiHandler;
import io.fi0x.javalogger.logging.LogEntry;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;
import processing.core.PImage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.function.Function;

public class MainSynthWindow extends PApplet
{
    private PImage icon;
    private String[] midiEntries;
    private int currentMidi;

    private final int xSynths = 4;
    private final int ySynths = 4;
    private final SynthUI[] synths = new SynthUI[xSynths * ySynths];
    private final ArrayList<CustomButton> buttons = new ArrayList<>();

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

        initializeSynths();

        new MidiHandler().initialize();

        fillControlBar();

        new Thread(() -> UDPProcessor.getInstance().run()).start();
    }
    @Override
    public void exitActual()
    {
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

        for(CustomButton button : buttons)
            button.draw();

        if(initialized < 100)
        {
            surface.setSize(displayWidth / 4 * 3, displayHeight / 4 * 3);
            initialized++;
        }
    }

    @Override
    public void mouseClicked()
    {
        for(CustomButton button : buttons)
        {
            if(button.interact(mouseX, mouseY))
            {
                buttons.get(2).updateText(midiEntries[currentMidi]);
                return;
            }
        }

        for(SynthUI s : synths)
        {
            if(s == null)
                continue;
            if(s.buttonClicked())
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
        Function<Integer, Boolean> openOrca = v ->
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
            return true;
        };
        buttons.add(new CustomButton("Open Orca", 5, height - 35, 100, 30, openOrca, this));

        Function<Integer, Boolean> openMixer = v ->
        {
            PApplet.main("com.fi0x.cc.project.mixer.gui.MainMixerWindow");
            return true;
        };
        buttons.add(new CustomButton("Open Mixer", 110, height - 35, 100, 30, openMixer, this));

        midiEntries = MidiHandler.getDeviceNames();
        Function<Integer, Boolean> nextMidiDevice = v ->
        {
            currentMidi++;
            if(currentMidi >= midiEntries.length)
                currentMidi = 0;

            SynthManager.setAcceptedMidi(midiEntries[currentMidi]);
            return true;
        };
        buttons.add(new CustomButton("Midi Device", 215, height - 35, 200, 30, nextMidiDevice, this));

        adjustControlBarPositions();
    }
    private void adjustControlBarPositions()
    {
        buttons.get(0).updatePosition(5, height - 35);
        buttons.get(1).updatePosition(110, height - 35);
        buttons.get(2).updatePosition(215, height - 35);
    }
}
