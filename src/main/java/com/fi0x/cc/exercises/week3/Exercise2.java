package com.fi0x.cc.exercises.week3;

import processing.core.PApplet;
import processing.core.PVector;

public class Exercise2 extends PApplet
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
            rect(0, 0, width, height);
            scale(0.9f);
            doRotation(rectRot);
        }
    }

    private void doRotation(float rot)
    {
        translate(width / 2f, height / 2f);
        rotate(rot);
        translate(-width / 2f, -height / 2f);
    }
}
