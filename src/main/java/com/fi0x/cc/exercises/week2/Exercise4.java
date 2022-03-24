package com.fi0x.cc.exercises.week2;

import processing.core.PApplet;

public class Exercise4 extends PApplet
{
    private final int EDGE_LENGTH = 20;
    private final int INNER_CIRCLE_SIZE = 10;
    private final int OUTER_CIRCLE_SIZE = 20;

    @Override
    public void settings()
    {
        int s = EDGE_LENGTH * OUTER_CIRCLE_SIZE * 2;
        size(s, s);
    }
    @Override
    public void setup()
    {
        frameRate(60);
        noStroke();
    }
    @Override
    public void draw()
    {
        background(150);

        for(int x = 0; x < EDGE_LENGTH; x++)
        {
            for(int y = 0; y < EDGE_LENGTH; y++)
            {
                fill(0);
                drawOuter(x * OUTER_CIRCLE_SIZE * 2 + OUTER_CIRCLE_SIZE, y * OUTER_CIRCLE_SIZE * 2 + OUTER_CIRCLE_SIZE);
                fill(255);
                drawInner(x * OUTER_CIRCLE_SIZE * 2 + OUTER_CIRCLE_SIZE, y * OUTER_CIRCLE_SIZE * 2 + OUTER_CIRCLE_SIZE);
            }
        }
    }

    private void drawOuter(int x, int y)
    {
        int mouseDistX = x - mouseX;
        int mouseDistY = y - mouseY;
        int xOff = x + (mouseDistX == 0 ? 0 : OUTER_CIRCLE_SIZE / mouseDistX);
        int yOff = y + (mouseDistY == 0 ? 0 : OUTER_CIRCLE_SIZE / mouseDistY);
        ellipse(xOff, yOff, OUTER_CIRCLE_SIZE, OUTER_CIRCLE_SIZE);
    }
    private void drawInner(int x, int y)
    {
        ellipse(x, y, INNER_CIRCLE_SIZE, INNER_CIRCLE_SIZE);
    }
}
