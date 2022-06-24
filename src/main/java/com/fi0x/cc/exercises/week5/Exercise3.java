package com.fi0x.cc.exercises.week5;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Exercise3 extends PApplet
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
            PVector v = c.getMouseVector(!mousePressed);
            v.normalize();
            c.accelerate(v.div(3));
            c.keepDistance();
            c.updateLocation();
            c.draw();
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
            repelBorder();
        }

        private void repelBorder()
        {
            if(currentX < 10)
                currentMovement.add(1, 0);
            if(currentX > width - 10)
                currentMovement.add(-1, 0);
            if(currentY < 10)
                currentMovement.add(0, 1);
            if(currentY > width - 10)
                currentMovement.add(0, -1);
        }
        private void keepDistance()
        {
            for(Circle c : circles)
            {
                float distSq = c.distSquared(currentX, currentY);
                if(distSq < 300)
                {
                    float xDiff = currentX - c.currentX;
                    float yDiff = currentY - c.currentY;
                    accelerate(new PVector(xDiff, yDiff).limit(1));
                }
            }
        }

        private float distSquared(float x, float y)
        {
            return sq(currentX - x) + sq(currentY - y);
        }

        private void draw()
        {
            pushMatrix();
            translate(currentX, currentY);
            rotate(atan2(currentMovement.y, currentMovement.x));
            triangle(0, -5, 0, 5, 15, 0);
            popMatrix();
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
