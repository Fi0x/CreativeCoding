package com.fi0x.cc.exercises.week4;

import processing.core.PApplet;

import java.util.ArrayList;

public class Exercise1 extends PApplet
{
    private ArrayList<Circle> circles = new ArrayList<>();

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
        Circle newCircle = new Circle(random(width), random(height));
        boolean valid = true;
        for(Circle c : circles)
        {
            if(c.isOverlapping(newCircle))
            {
                valid = false;
                break;
            }
        }
        if(valid)
            circles.add(newCircle);

        for(Circle c : circles)
            c.draw();
    }

    private class Circle
    {
        final float x;
        final float y;
        float r = 20;

        private Circle(float x, float y)
        {
            this.x = x;
            this.y = y;
        }

        private void draw()
        {
            if(canGrow())
                r+=0.25;

            ellipse(x, y, 2 * r, 2 * r);
        }

        boolean edges()
        {
            return x + r >= width || x - r <= 0 || y + r >= height || y - r <= 0;
        }

        private boolean canGrow()
        {
            if(edges())
                return false;

            boolean canGrow = true;
            for (Circle other : circles)
            {
                if(other == this)
                    continue;
                if(isOverlapping(other))
                {
                    canGrow = false;
                    break;
                }
            }
            return canGrow;
        }

        private boolean isOverlapping(Circle other)
        {
            return dist(x, y, other.x, other.y) <= r + other.r + 1;
        }
    }
}
