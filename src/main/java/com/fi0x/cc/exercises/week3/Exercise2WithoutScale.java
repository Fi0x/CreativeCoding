package com.fi0x.cc.exercises.week3;

import processing.core.PApplet;

public class Exercise2WithoutScale extends PApplet
{
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
        noFill();
    }

    @Override
    public void draw()
    {
        background(255);
        int rectCount = mouseX + 5;
        float rectRot = mouseY / (float) height;

        for(int i = 0; i < rectCount; i++)
        {
            float locationMultiplier = rectCount / (float) i * 0.01f;
            float sizeMultiplier = 1f / i * rectCount * 0.01f;
            rect(width / 2f - width / 2f * locationMultiplier, height / 2f - height / 2f * locationMultiplier, width * sizeMultiplier, height * sizeMultiplier);
            doRotation(rectRot);
        }
    }

    private void drawRect(int number)
    {
    }
    private void doRotation(float rot)
    {
        translate(width / 2f, height / 2f);
        rotate(rot);
        translate(-width / 2f, -height / 2f);
    }
}
