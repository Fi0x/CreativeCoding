package com.fi0x.cc.exercises.week8;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Exercise1 extends PApplet
{
    private final int maxBoids = 300;
    private final float maxSpeed = 3;
    private final ArrayList<Boid> flock = new ArrayList<>();
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
        frameRate(60f);
        fill(255);
        noStroke();

        for(int i = 0; i < maxBoids; i++)
            flock.add(new Boid());
    }

    @Override
    public void draw()
    {
        background(0);
        for(Boid b : flock)
        {
            b.update();
            b.show();
        }
    }

    private class Boid
    {
        private final PVector position;
        private final PVector velocity;
        private final PVector acceleration;
        private Boid()
        {
            position = new PVector(random(width), random(height));
            velocity = new PVector(random(-maxSpeed, maxSpeed), random(-maxSpeed, maxSpeed)).limit(maxSpeed);
            acceleration = velocity;
        }
        private void update()
        {
            calculateAcceleration();
            moveBoid();
        }
        private void show()
        {
            ellipse(position.x, position.y, 10, 10);
        }

        private void calculateAcceleration()
        {
            PVector newAcceleration = separate();
            newAcceleration.add(align());
            newAcceleration.add(cohesion());
            newAcceleration.limit(maxSpeed / 2);

            acceleration.add(newAcceleration);
            acceleration.limit(maxSpeed);
        }
        private void moveBoid()
        {
            velocity.add(acceleration);
            velocity.limit(maxSpeed);
            position.add(velocity);

            position.x %= width;
            position.y %= height;
        }

        private PVector separate()
        {
            PVector result = new PVector();

            //TODO

            return result;
        }
        private PVector align()
        {
            PVector result = new PVector();

            //TODO

            return result;
        }
        private PVector cohesion()
        {
            PVector result = new PVector();

            //TODO

            return result;
        }
    }
}
