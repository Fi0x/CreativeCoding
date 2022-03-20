package com.fi0x.cc.project.gui;

import processing.core.PApplet;

public class MainWindow extends PApplet
{
    @Override
    public void setup()
    {
        surface.setResizable(true);
//        surface.setIcon();
        surface.setSize(displayWidth / 2, displayHeight / 2);
        surface.setLocation(displayWidth / 4, displayHeight / 4);
        frameRate(60);
        background(0);
        noStroke();
    }
    @Override
    public void settings()
    {
    }
}
