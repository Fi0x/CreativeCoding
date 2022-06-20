package com.fi0x.cc.exercises.week5;

import processing.core.PApplet;

import java.util.ArrayList;

public class Exercise1 extends PApplet
{
    private float circleSizeModifier = 0.8f;
    private int rotation = 0;
    private int subCircles = 1;

    @Override
    public void settings()
    {
        size(500, 500);
    }
    @Override
    public void setup()
    {
        frameRate(60);
        fill(255);
        noStroke();
    }

    @Override
    public void draw()
    {
        background(0);

        float centerX = width / 2f;
        float centerY = height / 2f;
        translate(centerX, centerY);

        rotation++;
        if(rotation >= 360)
        {
            rotation = 0;
            subCircles++;
        }

        rotate(radians(rotation));
        drawSubCircle(width / 2f, height / 2f, subCircles, circleSizeModifier);
    }

    private void drawSubCircle(float ownXOffset, float ownYOffset, int followingSubCircles, float circleSizeModifier)
    {
        if(followingSubCircles == 0)
            ellipse(0, 0, width * circleSizeModifier, height * circleSizeModifier);
        else
        {
            for(int i = 0; i < 4; i++)
            {
                rotate(radians(360 / 4f));
                pushMatrix();
                translate(ownXOffset / 2f, ownYOffset / 2f);
                if(i % 2 == 0)
                    rotate(radians(rotation));
                else
                    rotate(radians(-rotation));

                drawSubCircle(ownXOffset / 2f, ownYOffset / 2f, followingSubCircles - 1, circleSizeModifier / 2f);
                popMatrix();
            }
        }
    }
}
