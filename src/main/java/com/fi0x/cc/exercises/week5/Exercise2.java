package com.fi0x.cc.exercises.week5;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Exercise2 extends PApplet
{
    private final ArrayList<Circle> circles = new ArrayList<>();

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
        fill(255);
        noStroke();

        for(int i = 0; i < 10; i++)
        {
            circles.add(new Circle());
        }
    }

    @Override
    public void draw()
    {
        background(0);

        for(Circle c : circles)
        {
            PVector v = c.getMouseVector(mousePressed);
            v.normalize();
            c.accelerate(v.div(3));
            c.updateLocation();
            ellipse(c.currentX, c.currentY, 20, 20);
        }
    }

    private class Circle
    {
        private float currentX;
        private float currentY;
        private final PVector currentMovement;

        private Circle()
        {
            currentX = random(width);
            currentY = random(height);
            currentMovement = new PVector(0, 0);
        }

        private PVector getMouseVector(boolean attract)
        {
            float xDiff = mouseX - currentX;
            float yDiff = mouseY - currentY;
            return new PVector(attract ? xDiff : -xDiff, attract ? yDiff : -yDiff);
        }
        private void accelerate(PVector force)
        {
            currentMovement.add(force);
            currentMovement.limit(4);
        }

        private void updateLocation()
        {
            currentX += currentMovement.x;
            currentY += currentMovement.y;

            if(currentX < 0)
                currentX = 0;
            if(currentX > width)
                currentX = width;
            if(currentY < 0)
                currentY = 0;
            if(currentY > height)
                currentY = height;
        }
    }
}
