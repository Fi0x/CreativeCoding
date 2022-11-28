package com.fi0x.cc.project.randomstuff.fractals;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class JuliaSet extends PApplet
{
    private final int MAX_ITERATIONS = 100;
    private final float COLOR_SHIFT = 0.3f;
    private float ca = 0;
    private float cb = 0;
    private float angleA = 0;
    private float angleB = 0;
    private float minX = -2;
    private float minY = -2;
    private float maxX = 2;
    private float maxY = 2;

    @Override
    public void settings()
    {
        size(900, 900);
        pixelDensity(1);
    }
    @Override
    public void exitActual()
    {
    }
    @Override
    public void setup()
    {
        frameRate(60f);
        fill(0,0);
        background(255);
        colorMode(HSB, MAX_ITERATIONS, 1, 1);

        updateImage();
    }
    @Override
    public void draw()
    {
    }
    @Override
    public void mouseWheel(MouseEvent event)
    {
        float mappedMouseX = map(mouseX, 0, width, minX, maxX);
        float mappedMouseY = map(mouseY, 0, height, minY, maxY);
        float xDistMin = mappedMouseX - minX;
        float xDistMax = maxX - mappedMouseX;
        float yDistMin = mappedMouseY - minY;
        float yDistMax = maxY - mappedMouseY;

        if(event.getCount() < 0)
        {
            minX += xDistMin / 2;
            maxX -= xDistMax / 2;
            minY += yDistMin / 2;
            maxY -= yDistMax / 2;
        }
        else
        {
            minX -= xDistMin * 2;
            maxX += xDistMax * 2;
            minY -= yDistMin * 2;
            maxY += yDistMax * 2;
        }
        updateImage();
    }

    @Override
    public void keyPressed(KeyEvent event)
    {
        if(event.getKeyCode() == RIGHT)
            angleA += 0.02f;
        else if(event.getKeyCode() == LEFT)
            angleA -= 0.02f;
        else if(event.getKeyCode() == UP)
            angleB += 0.02f;
        else if(event.getKeyCode() == DOWN)
            angleB -= 0.02f;

        updateImage();
    }
    private void updateImage()
    {
        ca = sin(angleA);
        cb = cos(angleB);

        loadPixels();
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                float hue = (getIterations(x, y) + (float) MAX_ITERATIONS * COLOR_SHIFT) % MAX_ITERATIONS;
                pixels[x + y * width] = color(hue, 0.7f, 0.7f);
            }
        }
        updatePixels();
    }
    private int getIterations(int x, int y)
    {
        int n = 0;

        float a = map(x, 0, width, minX, maxX);
        float b = map(y, 0, height, minY, maxY);

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
