package com.fi0x.cc.exercises.week6;

import processing.core.PApplet;

import java.util.ArrayList;

public class Exercise1 extends PApplet
{
    private ArrayList<PolarDesign> randomDesigns = new ArrayList<>();

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
        background(0);
        fill(255);
    }

    @Override
    public void draw()
    {
    }

    private class PolarDesign
    {
        //TODO: Add stuff to create a random Design
    }
}
