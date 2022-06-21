package com.fi0x.cc.exercises.week3;

import processing.core.PApplet;

public class Exercise2Variant extends PApplet
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
        frameRate(60);
        noFill();
    }

    @Override
    public void draw()
    {
        background(255);
        int rectCount = mouseX + 5;
        float rectRot = mouseY / (float) height;

        for(int i = 0; i < rectCount; i++)
        {
            rect(width/2f, height/2f, width/2f, height/2f);
            translate(width / 2f, height / 2f);
            rotate(rectRot);
            translate(-width / 2f, -height / 2f);
        }
    }

    private void drawRect(int number)
    {
    }
}
