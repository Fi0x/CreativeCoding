package com.fi0x.cc.exercises.week3;

import processing.core.PApplet;

public class Exercise3 extends PApplet
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
        background(0);
        stroke(255, 100);
        strokeWeight(width / 30f);

    }

    @Override
    public void draw()
    {
        if (!mousePressed)
            return;

        float centerX = width / 2f;
        float centerY = height / 2f;
        float mX = mouseX - centerX;
        float mY = mouseY - centerY;
        float pmX = pmouseX - centerX;
        float pmY = pmouseY - centerY;

        translate(centerX, centerY);
        for (float i = 0; i < 6; i++) {
            rotate(radians(360 / 6f));
            line(mX, mY, pmX, pmY);
            pushMatrix();
            scale(-1, 1);
            line(mX, mY, pmX, pmY);
            popMatrix();
        }

    }
}
