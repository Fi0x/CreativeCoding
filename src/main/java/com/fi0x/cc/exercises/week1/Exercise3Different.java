package com.fi0x.cc.exercises.week1;

import com.fi0x.cc.exercises.CustomMath;
import com.fi0x.cc.exercises.Startup;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;

import java.awt.*;

public class Exercise3Different extends PApplet
{
    private final int sizeX = 800;
    private final int sizeY = 800;
    private static final int colorRandomness = 50;
    private static final int alphaRandomness = 100;
    private static final int sizeRandomness = 10;

    private static Color mainTheme;

    @Override
    public void setup()
    {
        mainTheme = getRandomColor();
        background(mainTheme.getRed(), mainTheme.getGreen(), mainTheme.getBlue());
        stroke(0, 0);
        frameRate(500);
    }
    @Override
    public void settings()
    {
        size(sizeX, sizeY);
    }

    @Override
    public void keyPressed()
    {
        if(key == ' ')
        {
            mainTheme = getRandomColor();
            background(mainTheme.getRed(), mainTheme.getGreen(), mainTheme.getBlue());
        }
    }
    @Override
    public void draw()
    {
        int x = getRandAxisPos(sizeX, 5);
        int y = getRandAxisPos(sizeY, 5);
        int rad = (int) (Math.random() * sizeRandomness) + 10;

        Color c = getThemedColor();
        fill(c.getRed(), c.getGreen(), c.getBlue(), 255);

        ellipse(x, y, rad, rad);

        if(frameCount % 2500 == 0)
        {
            saveFrame("frames/exercise3/exercise3-####.png");
            Logger.getInstance().log("Frame saved", String.valueOf(Startup.LogTemplate.INFO_WHITE));
        }
    }

    private static int getRandAxisPos(int axisSize, int variationChance)
    {
        float returnValue = 0;
        for(int i = 0; i < variationChance; i++)
            returnValue += Math.random() * axisSize / variationChance;
        return (int) returnValue;
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