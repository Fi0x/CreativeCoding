package com.fi0x.cc.exercises.week4;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.ArrayList;

public class Exercise2 extends PApplet
{
    private final int INVADER_PIXELS = 7;
    private final int INVADER_SIZE = 20;

    private final ArrayList<Invader> invaders = new ArrayList<>();

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
        fill(255);
    }

    @Override
    public void draw()
    {
        background(0);

        if(frameCount % 60 == 0)
            invaders.add(new Invader());

        for(Invader i : invaders)
            i.drawInvader();
    }

    @Override
    public void mousePressed(MouseEvent event)
    {
        for(Invader i : invaders)
        {
            if(i.isMouseAbove())
            {
                invaders.remove(i);
                break;
            }
        }
    }

    private class Invader
    {
        private final PImage icon;
        private float x;
        private float y;
        private float speed;

        private Invader()
        {
            icon = generateInvader();
            y = 0;
            x = random(width - INVADER_PIXELS);
            speed = random(0.5f, 2);
        }

        private void drawInvader()
        {
            y += speed;
            image(icon, x, y, INVADER_SIZE, INVADER_SIZE);
        }

        private boolean isMouseAbove()
        {
            return mouseX >= x && mouseX <= x + INVADER_SIZE && mouseY >= y && mouseY <= y + INVADER_SIZE;
        }

        private PImage generateInvader()
        {
            int background = color(random(50), random(50), random(50));
            int primary = color(random(255), random(255), random(255));
            int secondary = color(random(255), random(255), random(255));
            int accent = color(random(255), random(255), random(255));

            PImage img = createImage(INVADER_PIXELS, INVADER_PIXELS, RGB);
            img.loadPixels();

            for(int x = 0; x <= (INVADER_PIXELS + 1) / 2; x++)
            {
                for(int y = 0; y < INVADER_PIXELS; y++)
                {
                    float colorChance = random(100);
                    int choosenColor = background;
                    if(colorChance < 10)
                        choosenColor = accent;
                    else if(colorChance < 30)
                        choosenColor = secondary;
                    else if(choosenColor < 60)
                        choosenColor = primary;

                    img.pixels[y * INVADER_PIXELS + x] = choosenColor;
                    img.pixels[y * INVADER_PIXELS + INVADER_PIXELS - x - 1] = choosenColor;
                }
            }
            img.updatePixels();
            return img;
        }
    }
}
