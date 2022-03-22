package com.fi0x.cc.exercises.week1;

import processing.core.PApplet;
import processing.core.PImage;

public class Exercise4Different extends PApplet
{
    private int sizeX = 800;
    private int sizeY = 800;
    private boolean isGrey = false;

    private PImage img;

    @Override
    public void setup()
    {
        frameRate(20);
        background(0);
        invertedImage();
        noStroke();
    }
    @Override
    public void settings()
    {
        img = loadImage("images/bridge.jpg");
        sizeX = (int) (img.width * 0.5);
        sizeY = (int) (img.height * 0.5);
        size(sizeX, sizeY);
    }

    @Override
    public void keyPressed()
    {
        if(key == ' ')
        {
            if(isGrey)
                invertedImage();
            else
                greyImage();
        }
    }

    @Override
    public void draw()
    {
        fill(255);
        ellipse((float) sizeX / 2, (float) sizeY / 2, 10, 10);

        if(frameCount % 400 == 0)
        {
            if(isGrey)
                invertedImage();
            else
                greyImage();
        }
    }
    private void greyImage()
    {
        image(img, 0, 0, sizeX, sizeY);
        filter(GRAY);
        isGrey = true;
    }
    private void invertedImage()
    {
        image(img, 0, 0, sizeX, sizeY);
        filter(INVERT);
        isGrey = false;
    }
}