package com.fi0x.cc.week1;

import processing.core.PApplet;
import processing.core.PImage;

public class Exercise4 extends PApplet
{
    private int sizeX = 800;
    private int sizeY = 800;

    private PImage img;

    @Override
    public void setup()
    {
        frameRate(5);
        background(0);
        noStroke();
    }
    @Override
    public void settings()
    {
        img = loadImage("bridge.jpg");
        sizeX = (img.width);
        sizeY = (img.height);
        size(sizeX, sizeY);
    }

    @Override
    public void keyPressed()
    {
        if(key == ' ')
            image(img, 0, 0, sizeX, sizeY);
    }
    @Override
    public void draw()
    {
        float circleSize = 30;

        for (int x = 0; x < width; x += circleSize)
        {
            for (int y = -47; y < height; y += circleSize)
            {
                fill(img.get(x, max(y, 0)));
                ellipse(x, y, circleSize, circleSize);
            }
            circleSize *= 0.980;
            circleSize = max(circleSize, 1);
        }
    }
}