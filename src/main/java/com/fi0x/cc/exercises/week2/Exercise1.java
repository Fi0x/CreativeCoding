package com.fi0x.cc.exercises.week2;

import processing.core.PApplet;

public class Exercise1 extends PApplet
{
    private final int SIZE = 7;
    private final int TILE_SIZE = 20;

    @Override
    public void settings()
    {
        size(SIZE * TILE_SIZE, SIZE * TILE_SIZE);
        smooth(0);
    }
    @Override
    public void setup()
    {
        frameRate(1);
        background(0);
        noStroke();
    }

    @Override
    public void draw()
    {
        int primary = color(random(255), random(255), random(255));
        int secondary = color(random(255), random(255), random(255));
        int accent = color(random(255), random(255), random(255));

        background(random(150), random(150), random(150));

        for(int x = 0; x <= (SIZE + 1) / 2; x++)
        {
            for(int y = 0; y <= SIZE; y++)
            {
                float colorChance = random(100);
                if(colorChance < 40)
                    continue;
                else if(colorChance < 70)
                    fill(primary);
                else
                    fill(colorChance < 90 ? secondary : accent);

                drawMirroredPixels(x, y);
            }
        }
    }

    private void drawMirroredPixels(int x, int y)
    {
        rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        rect((SIZE - x - 1) * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
