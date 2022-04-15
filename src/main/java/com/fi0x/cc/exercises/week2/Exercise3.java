package com.fi0x.cc.exercises.week2;

import processing.core.PApplet;

public class Exercise3 extends PApplet
{
    private final int COLUMNS = 32;
    private final int ROWS = 18;
    private final int CIRCLE_SIZE = 10;

    @Override
    public void settings()
    {
        size(COLUMNS * CIRCLE_SIZE, ROWS * CIRCLE_SIZE);
    }
    @Override
    public void setup()
    {
        frameRate(1);
        noStroke();
    }
    @Override
    public void draw()
    {
        background(0);
        for(int x = 0; x < COLUMNS; x++)
        {
            for(int y = 0; y < ROWS; y++)
            {
                int color = color(random(55 + ((float) x / COLUMNS) * 200), 100 + random(50), 100 + random(50));
                fill(color);
                drawQuarter(x * CIRCLE_SIZE, y * CIRCLE_SIZE);
            }
        }
    }

    private void drawQuarter(int x, int y)
    {
        int rotation = (int) random(3);
        switch(rotation)
        {
            case 1:
                x += CIRCLE_SIZE;
                break;
            case 2:
                x += CIRCLE_SIZE;
                y += CIRCLE_SIZE;
                break;
            case 3:
                y += CIRCLE_SIZE;
                break;
        }
        arc(x, y, CIRCLE_SIZE * 2, CIRCLE_SIZE * 2, rotation * HALF_PI, (rotation + 1) * HALF_PI);
    }
}
