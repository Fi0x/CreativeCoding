package com.fi0x.cc.project.randomstuff.fractals;

import processing.core.PApplet;
import processing.event.MouseEvent;

public class MandelbrotDouble extends PApplet
{
    private final int MAX_ITERATIONS = 100;
    private final float COLOR_SHIFT = 0.3f;
    private double minX = -2;
    private double minY = -2;
    private double maxX = 2;
    private double maxY = 2;

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
        frameRate(120f);
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
        double mappedMouseX = map(mouseX, 0, width, minX, maxX);
        double mappedMouseY = map(mouseY, 0, height, minY, maxY);
        double xDistMin = mappedMouseX - minX;
        double xDistMax = maxX - mappedMouseX;
        double yDistMin = mappedMouseY - minY;
        double yDistMax = maxY - mappedMouseY;

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
    public void mousePressed()
    {
        double mappedMouseX = map(mouseX, 0, width, minX, maxX);
        double mappedMouseY = map(mouseY, 0, height, minY, maxY);
        System.out.println(mappedMouseX + " " + mappedMouseY);
    }
    private void updateImage()
    {
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

        double a = map(x, 0, width, minX, maxX);
        double b = map(y, 0, height, minY, maxY);
        double ca = a;
        double cb = b;

        for(; n < MAX_ITERATIONS; n++)
        {
            double aa = a * a - b * b;
            double bb = 2 * a * b;
            a = aa + ca;
            b = bb + cb;

            if(a * a + b * b > 16)
                break;
        }

        return n;
    }

    private double map(double value, double originalStart, double originalEnd, double newStart, double newEnd)
    {
        double originalPercent = value / (originalEnd - originalStart);
        return (newEnd - newStart) * originalPercent + newStart;
    }
}
