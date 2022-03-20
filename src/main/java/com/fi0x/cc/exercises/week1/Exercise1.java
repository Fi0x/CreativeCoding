package com.fi0x.cc.exercises.week1;

import processing.core.PApplet;

public class Exercise1 extends PApplet
{
    private int r = 0;
    private int g = 0;
    private int b = 0;

    @Override
    public void setup()
    {
        background(0, 0, 255);
    }
    @Override
    public void settings()
    {
        size(500, 400);
    }
    @Override
    public void draw()
    {
        stroke(0, 0, 0, 0);
        fill(r, g, b);
        rect(250, 200, 150, 100);
        if(mousePressed)
        {
            fill(255, 0, 0);
            ellipse(mouseX, mouseY, 10, 10);
        }

        r += Math.random() * 30 - 15;
        g += Math.random() * 30 - 15;
        b += Math.random() * 30 - 15;
        if(r > 255)
            r = 255;
        if(g > 255)
            g = 255;
        if(b > 255)
            b = 255;
        if(r < 0)
            r = 0;
        if(g < 0)
            g = 0;
        if(b < 0)
            b = 0;
    }
}
