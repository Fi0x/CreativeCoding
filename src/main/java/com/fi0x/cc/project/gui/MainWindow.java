package com.fi0x.cc.project.gui;

import processing.core.PApplet;
import processing.core.PImage;

public class MainWindow extends PApplet
{
    private PImage icon;

    @Override
    public void setup()
    {
        surface.setResizable(true);
        surface.setIcon(icon);
        surface.setTitle("Computer-Grafik-Projekt");
        surface.setSize(displayWidth / 2, displayHeight / 2);
        surface.setLocation(displayWidth / 4, displayHeight / 4);
        frameRate(60);
        background(0);
        noStroke();
    }
    @Override
    public void settings()
    {
        icon = loadImage("logo.jpg");
    }
}
