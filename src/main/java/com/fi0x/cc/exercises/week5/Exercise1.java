package com.fi0x.cc.exercises.week5;

import processing.core.PApplet;

import java.util.ArrayList;

public class Exercise1 extends PApplet
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
        fill(255, 0, 0);
        ellipse(0, 0, 50, 50);
        translate(100, 100);
        ellipse(0, 0, 50, 50);
        scale(0.5f);
        translate(100, 100);
        ellipse(0, 0, 50, 50);
    }
}
