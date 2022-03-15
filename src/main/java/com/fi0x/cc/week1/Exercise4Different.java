package com.fi0x.cc.week1;

import processing.core.PApplet;
import processing.core.PImage;

public class Exercise4Different extends PApplet
{
    private int sizeX = 800;
    private int sizeY = 800;

    private PImage img;

    @Override
    public void setup()
    {
        frameRate(5);
        background(0);
        stroke(0, 0);
    }
    @Override
    public void settings()
    {
        img = loadImage("bridge.jpg");
        sizeX = (int) (img.width * 0.5);
        sizeY = (int) (img.height * 0.5);
        size(sizeX, sizeY);
    }

    @Override
    public void keyPressed()
    {
        if(key == ' ')
            background(0);
    }

    @Override
    public void draw()
    {
        tint(150, 130, 200, 255);

        img.loadPixels();
        for(int i = 0; i < img.pixels.length; i++)
        {
            float r = red(img.pixels[i]);
            float g = green(img.pixels[i]);
            float b = blue(img.pixels[i]);

            img.pixels[i] = color((r+g+b) / 3);
        }

        image(img, 0, 0, sizeX, sizeY);
    }
}