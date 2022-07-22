package com.fi0x.cc.project.randomstuff;

import processing.core.PApplet;

public class ColorPicker extends PApplet
{
    @Override
    public void settings()
    {
        size(500, 500);
    }
    @Override
    public void exitActual()
    {
    }
    @Override
    public void setup()
    {
        frameRate(60f);
        fill(255);
        noStroke();
    }

    @Override
    public void draw()
    {
        background(0);
    }
}
