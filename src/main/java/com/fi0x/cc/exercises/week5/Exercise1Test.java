package com.fi0x.cc.exercises.week5;

import processing.core.PApplet;

public class Exercise1Test extends PApplet
{
    @Override
    public void settings()
    {
        size(500, 500);
    }
    @Override
    public void setup()
    {
        frameRate(60);
        background(0);
        fill(255);
        noStroke();
    }

    @Override
    public void draw()
    {
        fill(0, 255, 0);
        ellipse(0, 0, 50, 50);
        ellipse(100, 100, 50, 50);
        ellipse(200, 200, 50, 50);
        fill(255, 0, 0);
        ellipse(0, 0, 30, 30);
        translate(100, 100);
        ellipse(0, 0, 30, 30);
        scale(0.5f);
        translate(100, 100);
        ellipse(0, 0, 30, 30);
    }
}
