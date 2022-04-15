package com.fi0x.cc.exercises.week2;

import processing.core.PApplet;
import processing.core.PImage;

public class Exercise2 extends PApplet
{
    private final int INVADER_SIZE = 7;
    private final int INVADERS = 20;
    private final int INVADER_DISPLAY_MULTI = 4;

    private int invadersDrawn = 0;

    @Override
    public void settings()
    {
        int size = INVADER_SIZE * INVADERS * INVADER_DISPLAY_MULTI;
        size(size, size);
        smooth(0);
    }
    @Override
    public void setup()
    {
        frameRate(20);
        background(0);
        noStroke();
    }
    @Override
    public void draw()
    {
        if(invadersDrawn > INVADERS * INVADERS)
            invadersDrawn = 0;

        PImage invader = generateInvader();
        int invaderX = invadersDrawn % INVADERS * INVADER_SIZE * INVADER_DISPLAY_MULTI;
        int invaderY = invadersDrawn / INVADERS * INVADER_SIZE * INVADER_DISPLAY_MULTI;
        image(invader, invaderX + 1, invaderY + 1, INVADER_DISPLAY_MULTI * INVADER_SIZE - 2, INVADER_DISPLAY_MULTI * INVADER_SIZE - 2);

        invadersDrawn++;
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
                int chosenColor = background;
                if(colorChance < 10)
                    chosenColor = accent;
                else if(colorChance < 30)
                    chosenColor = secondary;
                else if(chosenColor < 60)
                    chosenColor = primary;

                img.pixels[y * INVADER_SIZE + x] = chosenColor;
                img.pixels[y * INVADER_SIZE + INVADER_SIZE - x - 1] = chosenColor;
            }
        }
        img.updatePixels();
        return img;
    }
}
