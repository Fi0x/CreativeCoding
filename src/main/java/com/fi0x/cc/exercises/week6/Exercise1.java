package com.fi0x.cc.exercises.week6;

import processing.core.PApplet;
import processing.core.PVector;

public class Exercise1 extends PApplet
{
    private final int columns = 5;
    private final int rows = 5;
    private final int designSize = 100;

    @Override
    public void settings()
    {
        size(columns * (designSize + 10), rows * (designSize + 10));
        smooth(4);
    }
    @Override
    public void exitActual()
    {
    }
    @Override
    public void setup()
    {
        frameRate(0.2f);
        noFill();
        randomSeed(90284570918234345L);
        create();
    }

    @Override
    public void draw()
    {
        create();
    }

    private void create()
    {
        background(0);
        for(int x = 0; x < columns; x++)
        {
            for(int y = 0; y < rows; y++)
            {
                pushMatrix();
                translate(x * (designSize + 10), y * (designSize + 10));
                translate((designSize + 5) / 2f, (designSize + 5) / 2f);
                drawDesign();
                popMatrix();
            }
        }
    }

    private void drawDesign()
    {
        stroke(random(255), random(255), random(255));
        float a = random(0.1f, 10);
        PVector first = new PVector(sin(0) * designSize * 0.5f * cos(0), cos(0) * designSize * 0.5f * cos(0));
        beginShape();
        float x = 1000;
        float y = 1000;
        for(float deg = 0.1f; abs(min(x - first.x, first.x - x)) > 3 || abs(min(y - first.y, first.y - y)) > 3; deg += 0.1)
        {
            float r = designSize * 0.5f * cos(deg * a);
            x = sin(deg) * r;
            y = cos (deg) * r;
            vertex(x, y);
        }
        endShape();
    }
}
