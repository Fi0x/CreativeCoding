package com.fi0x.cc.exercises.week1;

import com.fi0x.cc.exercises.Startup;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;

import java.awt.*;

public class Exercise3Different extends PApplet
{
    private static final int colorRandomness = 50;
    private static final int alphaRandomness = 100;
    private static final int sizeRandomness = 10;

    private static Color mainTheme;

    @Override
    public void setup()
    {
        mainTheme = Exercise3.getRandomColor();
        background(mainTheme.getRed(), mainTheme.getGreen(), mainTheme.getBlue());
        stroke(0, 0);
        frameRate(500);
    }
    @Override
    public void settings()
    {
        int sizeX = 800;
        int sizeY = 800;
        size(sizeX, sizeY);
    }

    @Override
    public void keyPressed()
    {
        if(key == ' ')
        {
            mainTheme = Exercise3.getRandomColor();
            background(mainTheme.getRed(), mainTheme.getGreen(), mainTheme.getBlue());
        }
    }
    @Override
    public void draw()
    {
        int x = getRandAxisPos();
        int y = getRandAxisPos();
        int rad = (int) (Math.random() * sizeRandomness) + 10;

        Color c = Exercise3.getThemedColor();
        fill(c.getRed(), c.getGreen(), c.getBlue(), 255);

        ellipse(x, y, rad, rad);

        if(frameCount % 2500 == 0)
        {
            saveFrame("frames/exercise3/exercise3-####.png");
            Logger.log("Frame saved", String.valueOf(Startup.LogTemplate.INFO_WHITE));
        }
    }

    private static int getRandAxisPos()
    {
        float returnValue = 0;
        for(int i = 0; i < 5; i++)
            returnValue += Math.random() * 800 / 5;
        return (int) returnValue;
    }
}