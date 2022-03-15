package com.fi0x.cc.week1;

import processing.core.PApplet;
import processing.core.PImage;

public class Exercise4Pointillism extends PApplet
{
    private int sizeX = 800;
    private int sizeY = 800;

    private PImage img;

    @Override
    public void setup()
    {
        frameRate(5000);
        image(img, 0, 0, sizeX, sizeY);
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
            image(img, 0, 0, sizeX, sizeY);
    }
    @Override
    public void draw()
    {
        int x = (int) random(img.width);
        int y = (int) random(img.height);
        int loc = x + y * img.width;

        loadPixels();
        float r = red(img.pixels[loc]);
        float g = green(img.pixels[loc]);
        float b = blue(img.pixels[loc]);
        noStroke();

        fill(r,g,b,100);
        ellipse(x / 2,y / 2,10,10);
    }
}