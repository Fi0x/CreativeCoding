package com.fi0x.cc.exercises.week4;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Exercise2 extends PApplet
{
    private final int INVADER_SIZE = 7;

    @Override
    public void settings()
    {
        size(500, 500);
    }
    @Override
    public void setup()
    {
        frameRate(60);
        background(0);
        fill(255);
    }

    @Override
    public void draw()
    {
        Invader i = new Invader();

        image(i.image, 0, 0, INVADER_SIZE, INVADER_SIZE);
    }

    private class Invader
    {
        private final PImage image;

        private Invader()
        {
            image = generateInvader();
        }

        private PImage generateInvader()
        {
            int background = color(random(50), random(50), random(50));
            int primary = color(random(255), random(255), random(255));
            int secondary = color(random(255), random(255), random(255));
            int accent = color(random(255), random(255), random(255));

            PImage img = createImage(INVADER_SIZE, INVADER_SIZE, RGB);
            img.loadPixels();

            for(int x = 0; x <= (INVADER_SIZE + 1) / 2; x++)
            {
                for(int y = 0; y < INVADER_SIZE; y++)
                {
                    float colorChance = random(100);
                    int choosenColor = background;
                    if(colorChance < 10)
                        choosenColor = accent;
                    else if(colorChance < 30)
                        choosenColor = secondary;
                    else if(choosenColor < 60)
                        choosenColor = primary;

                    img.pixels[y * INVADER_SIZE + x] = choosenColor;
                    img.pixels[y * INVADER_SIZE + INVADER_SIZE - x - 1] = choosenColor;
                }
            }
            img.updatePixels();
            return img;
        }
    }
}
