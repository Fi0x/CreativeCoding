package com.fi0x.cc.project.randomstuff;

import processing.core.PApplet;
import processing.event.MouseEvent;

public class Mandelbrot extends PApplet
{
    private final int MAX_ITERATIONS = 100;
    private float minValue = -2;
    private float maxValue = 2;

    @Override
    public void settings()
    {
        size(900, 900);
    }
    @Override
    public void exitActual()
    {
    }
    @Override
    public void setup()
    {
        frameRate(120f);
        fill(0,0);
        background(255);
        pixelDensity = 1;
        updateImage();
    }
    @Override
    public void draw()
    {
    }
    @Override
    public void mouseWheel(MouseEvent event)
    {
        if(event.getCount() < 0)
        {
            minValue /= 2;
            maxValue /= 2;
        }
        else
        {
            minValue *= 2;
            maxValue *= 2;
        }

        updateImage();
    }

    private void updateImage()
    {
        loadPixels();
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                float bright = map(getIterations(x, y), 0, MAX_ITERATIONS, 255, 0);
                pixels[x + y * width] = color(bright);
            }
        }
        updatePixels();
    }
    private int getIterations(int x, int y)
    {
        int n = 0;

        float a = map(x, 0, width, minValue, maxValue);
        float b = map(y, 0, height, minValue, maxValue);
        float ca = a;
        float cb = b;

        for(; n < MAX_ITERATIONS; n++)
        {
            float aa = a * a - b * b;
            float bb = 2 * a * b;
            a = aa + ca;
            b = bb + cb;

            if(a * a + b * b > 16)
                break;
        }

        return n;
    }
}
