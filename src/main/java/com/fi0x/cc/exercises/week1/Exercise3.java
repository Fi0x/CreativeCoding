package com.fi0x.cc.exercises.week1;

import com.fi0x.cc.exercises.CustomMath;
import processing.core.PApplet;

import java.awt.*;

public class Exercise3 extends PApplet
{
    private final int sizeX = 800;
    private final int sizeY = 800;
    private static final int colorRandomness = 50;
    private static final int alphaRandomness = 100;
    private static final int sizeRandomness = 10;
    private int maxIterations = 500;

    private static Color mainTheme;
    private int iterations = 0;

    @Override
    public void setup()
    {
        mainTheme = getRandomColor();
        background(mainTheme.getRed(), mainTheme.getGreen(), mainTheme.getBlue());
        stroke(0, 0);
    }
    @Override
    public void settings()
    {
        size(sizeX, sizeY);
    }
    @Override
    public void draw()
    {
        if(iterations > maxIterations)
        {
            mainTheme = getRandomColor();
            background(mainTheme.getRed(), mainTheme.getGreen(), mainTheme.getBlue());
            iterations = 0;
            maxIterations += Math.random() * maxIterations - maxIterations / 2;
        }
        int x = (int) (Math.random() * sizeX);
        int y = (int) (Math.random() * sizeY);
        int rad = (int) (Math.random() * (sizeRandomness + x * 40 / sizeX + y * 40 / sizeY)) + 10;

        Color c = getThemedColor();
        fill(c.getRed(), c.getGreen(), c.getBlue(), 255);

        ellipse(x, y, rad, rad);
        iterations++;
    }

    private static Color getRandomColor()
    {
        int r = (int)(Math.random() * 256);
        int g = (int)(Math.random() * 256);
        int b = (int)(Math.random() * 256);
        int a = (int)(Math.random() * 256);
        return new Color(r, g, b, a);
    }

    private static Color getThemedColor()
    {
        int r = (int) (Math.random() * colorRandomness) - colorRandomness / 2 + mainTheme.getRed();
        int g = (int) (Math.random() * colorRandomness) - colorRandomness / 2 + mainTheme.getGreen();
        int b = (int) (Math.random() * colorRandomness) - colorRandomness / 2 + mainTheme.getBlue();
        int a = (int) (Math.random() * alphaRandomness) - alphaRandomness / 2 + mainTheme.getAlpha();

        return new Color(
                CustomMath.clamp(r, 0, 255),
                CustomMath.clamp(g, 0, 255),
                CustomMath.clamp(b, 0, 255),
                CustomMath.clamp(a, 0, 255)
        );
    }
}
