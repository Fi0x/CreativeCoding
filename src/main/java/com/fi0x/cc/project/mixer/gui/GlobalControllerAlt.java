package com.fi0x.cc.project.mixer.gui;

import com.fi0x.cc.project.mixer.MixerManager;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PConstants;

public class GlobalControllerAlt
{
    //FIXME: Translation is currently not working for P5Controls
    //TODO: Remake to use custom UI elements that can be translated correctly
    private final PApplet parent;
    private final ControlP5 control;

    public GlobalControllerAlt(PApplet parentScreen)
    {
        parent = parentScreen;
        control = new ControlP5(parent);

        control.addButton("BPM+")
                .setSize(40, 20)
                .setPosition(100, 10)
                .setValue(0);
        control.addButton("BPM-")
                .setSize(40, 20)
                .setPosition(10, 10)
                .setValue(0);
        control.addButton("Notes+")
                .setSize(40, 20)
                .setPosition(100, 35)
                .setValue(0);
        control.addButton("Notes-")
                .setSize(40, 20)
                .setPosition(10, 35)
                .setValue(0);
    }

    public void draw()
    {
        parent.fill(255);
        parent.textSize(12);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text(MixerManager.getBPM(), 75, 20);
        parent.text(MixerManager.getNotesPerBeat(), 75, 45);
    }

    public void buttonClicked(ControlEvent event)
    {
        if(event.getController() == control.getController("BPM+"))
            MixerManager.changeBPM(1);
        else if(event.getController() == control.getController("BPM-"))
            MixerManager.changeBPM(-1);
        else if(event.getController() == control.getController("Notes+"))
            MixerManager.changeNPB(1);
        else if(event.getController() == control.getController("Notes-"))
            MixerManager.changeNPB(-1);
    }
}
