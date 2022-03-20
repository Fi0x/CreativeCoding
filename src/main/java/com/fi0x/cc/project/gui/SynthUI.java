package com.fi0x.cc.project.gui;

import com.fi0x.cc.project.synth.SynthManager;
import com.fi0x.cc.project.synth.synthesizers.ISynthesizer;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;

public class SynthUI
{
    private final ISynthesizer linkedSynth;

    private final int x;
    private final int y;
    private final int xSize;
    private final int ySize;

    private final PApplet parentScreen;
    private ControlP5 control;
    private Button btn;

    public SynthUI(PApplet parent, int xPos, int yPos, int w, int h) throws IllegalStateException
    {
        linkedSynth = SynthManager.getNextSynth();
        if(linkedSynth == null)
            throw new IllegalStateException("No Synths loaded that could be linked.");

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

        parentScreen.fill(255);
        parentScreen.rect(5, 5, xSize - 10, ySize - 10);

        parentScreen.translate(-x, -y);
    }

    public void buttonClicked(ControlEvent event)
    {
        if(event.getController() == control.getController("Previous Synth"))
            linkedSynth.previousInstrument();
        if(event.getController() == control.getController("Next Synth"))
            linkedSynth.nextInstrument();
    }

    private void addButtons()
    {
        control.addButton("Previous Synth")
                .setPosition(x + 10, y + 10)
                .setSize(100, 30)
                .setValue(0);
        control.addButton("Next Synth")
                .setPosition(x + xSize - 100 - 10, y + 10)
                .setSize(100, 30)
                .setValue(0);
    }
}
