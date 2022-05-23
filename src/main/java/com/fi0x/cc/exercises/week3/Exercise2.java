package com.fi0x.cc.exercises.week3;

import processing.core.PApplet;
import processing.core.PVector;

public class Exercise2 extends PApplet
{
    @Override
    public void settings()
    {
        size(500, 500);
        noFill();
    }
    @Override
    public void setup()
    {
        frameRate(60);
    }

    @Override
    public void draw()
    {
        background(255);
        int rectCount = mouseX + 5;
        int rectRot = mouseY;

        for(int i = 0; i < rectCount; i++)
        {
            rect(width/2f, height/2f, width/2f, height/2f);
        }
    }

    private void drawRect(int number)
    {
    }
}
