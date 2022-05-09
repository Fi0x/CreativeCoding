package com.fi0x.cc.exercises.week3;

import processing.core.PApplet;
import processing.core.PVector;

public class Exercise1 extends PApplet
{
    private final int RECT_SIZE = 20;
    private final PVector RECTS = new PVector(10, 20);

    private float offset = 0;
    private boolean reverse = false;
    private long seed = 93248592;

    @Override
    public void settings()
    {
        size((int) ((RECTS.x + 2) * RECT_SIZE), (int) ((RECTS.y + 2) * RECT_SIZE));
    }
    @Override
    public void setup()
    {
        frameRate(60);
        background(0);
    }

    @Override
    public void draw()
    {
        randomSeed(seed);
        background(255);
        fill(0, 0, 0, 0);

        translate(0, RECT_SIZE);
        for (int i = 0; i < RECTS.x; i ++)
        {
            translate(RECT_SIZE, 0);
            pushMatrix();
            for (int j = 0; j < RECTS.y; j ++)
            {
                drawRect(j);
                translate(0, RECT_SIZE);
            }
            popMatrix();
        }

        if(reverse)
            offset -= 0.01;
        else
            offset += 0.01;

        if(offset > 1)
            reverse = true;
        else if(offset < -1)
            reverse = false;
    }

    private void drawRect(int number)
    {
        float multiplier = random(0.5f, 1.5f) * number * offset;
        translate(0, multiplier);
        pushMatrix();
        rotate(multiplier * 0.1f);
        rect(0, 0, RECT_SIZE, RECT_SIZE);
        popMatrix();
        translate(0, -multiplier);
    }
}
