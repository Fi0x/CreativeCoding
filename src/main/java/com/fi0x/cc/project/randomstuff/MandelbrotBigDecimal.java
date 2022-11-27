package com.fi0x.cc.project.randomstuff;

import processing.core.PApplet;
import processing.event.MouseEvent;

import java.math.BigDecimal;
import java.math.MathContext;

public class MandelbrotBigDecimal extends PApplet
{
    private final int MAX_ITERATIONS = 100;
    private final int PRECISION = 10;
    private final float COLOR_SHIFT = 0.3f;
    private BigDecimal minX = new BigDecimal(-2);
    private BigDecimal minY = new BigDecimal(-2);
    private BigDecimal maxX = new BigDecimal(2);
    private BigDecimal maxY = new BigDecimal(2);

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
        BigDecimal mappedMouseX = map(new BigDecimal(mouseX), new BigDecimal(width), minX, maxX);
        BigDecimal mappedMouseY = map(new BigDecimal(mouseY), new BigDecimal(height), minY, maxY);
        BigDecimal xDistMin = mappedMouseX.subtract(minX);
        BigDecimal xDistMax = maxX.subtract(mappedMouseX);
        BigDecimal yDistMin = mappedMouseY.subtract(minY);
        BigDecimal yDistMax = maxY.subtract(mappedMouseY);

        if(event.getCount() < 0)
        {
            minX = minX.add(xDistMin.divide(new BigDecimal(2), new MathContext(PRECISION)));
            maxX = maxX.subtract(xDistMax.divide(new BigDecimal(2), new MathContext(PRECISION)));
            minY = minY.add(yDistMin.divide(new BigDecimal(2), new MathContext(PRECISION)));
            maxY = maxY.subtract(yDistMax.divide(new BigDecimal(2), new MathContext(PRECISION)));
        }
        else
        {
            minX = minX.subtract(xDistMin.multiply(new BigDecimal(2)));
            maxX = maxX.add(xDistMax.multiply(new BigDecimal(2)));
            minY = minY.subtract(yDistMin.multiply(new BigDecimal(2)));
            maxY = maxY.add(yDistMax.multiply(new BigDecimal(2)));
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
                float hue = (getIterations(x, y) + (float) MAX_ITERATIONS * COLOR_SHIFT) % MAX_ITERATIONS;
                pixels[x + y * width] = color(hue, 0.7f, 0.7f);
            }
        }
        updatePixels();
    }
    private int getIterations(int x, int y)
    {
        int n = 0;

        BigDecimal a = map(new BigDecimal(x), new BigDecimal(width), minX, maxX);
        BigDecimal b = map(new BigDecimal(y), new BigDecimal(height), minY, maxY);
        BigDecimal ca = a;
        BigDecimal cb = b;

        for(; n < MAX_ITERATIONS; n++)
        {
            BigDecimal aa = a.multiply(a).subtract(b.multiply(b));
            BigDecimal bb = a.multiply(b).multiply(new BigDecimal(2));
            a = aa.add(ca);
            b = bb.add(cb);

            if(a.multiply(a).add(b.multiply(b)).compareTo(new BigDecimal(16)) > 0)
                break;
        }

        return n;
    }

    private BigDecimal map(BigDecimal value, BigDecimal originalEnd, BigDecimal newStart, BigDecimal newEnd)
    {
        BigDecimal newDist = newEnd.subtract(newStart);
        BigDecimal originalPercent = value.divide(originalEnd, new MathContext(PRECISION));
        return originalPercent.multiply(newDist).add(newStart);
    }
}
