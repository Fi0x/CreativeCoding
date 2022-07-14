package com.fi0x.cc.exercises.week8;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Exercise1Mouse extends PApplet
{
    private final int maxBoids = 100;
    private final float maxSpeed = 3;
    private final float minDistance = 50;
    private final float cohesionDistance = 100;
    private final float cohesionMultiplier = 0.9f;
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
            pushMatrix();
            translate(position.x, position.y);
            rotate(atan2(velocity.y, velocity.x));
            triangle(0, -5, 0, 5, 15, 0);
            popMatrix();
        }

        private void calculateAcceleration()
        {
            PVector newAcceleration = separate();
            newAcceleration.add(align());
            newAcceleration.add(cohesion());
            newAcceleration.add(fleeMouse());
            newAcceleration.limit(maxSpeed / 2);

            acceleration.add(newAcceleration);
            acceleration.limit(maxSpeed);
        }
        private void moveBoid()
        {
            velocity.add(acceleration);
            velocity.limit(maxSpeed);
            position.add(velocity);

            if(position.x < 0)
                position.x = width;
            else if(position.x > width)
                position.x = 0;
            if(position.y < 0)
                position.y = height;
            else if(position.y > height)
                position.y = 0;
        }

        private PVector separate()
        {
            PVector steer = new PVector();

            for(Boid other : flock)
            {
                if(other == this)
                    continue;

                float d = getDistanceTo(other.position);
                if(d < minDistance)
                {
                    PVector diff = PVector.sub(position, other.position);
                    diff.normalize();
                    diff.div(d);
                    steer.add(diff);
                }
            }

            steer.limit(maxSpeed);
            return steer;
        }
        private PVector align()
        {
            PVector steer = new PVector();

            for(Boid other : flock)
            {
                if(other == this)
                    continue;

                float d = getDistanceTo(other.position);
                if(d > cohesionDistance)
                    continue;

                PVector direction = other.velocity;

                direction.normalize();
                direction.div(d);

                steer.add(direction);
            }

            steer.limit(maxSpeed);
            return steer;
        }
        private PVector cohesion()
        {
            PVector steer = new PVector();

            for(Boid other : flock)
            {
                if(other == this)
                    continue;

                float d = getDistanceTo(other.position);
                if(d < cohesionDistance && d > minDistance)
                {
                    PVector diff = PVector.sub(other.position, position);
                    diff.normalize();
                    diff.div(d);
                    steer.add(diff);
                }
            }

            steer.limit(maxSpeed * cohesionMultiplier);
            return steer;
        }
        private PVector fleeMouse()
        {
            PVector steer = new PVector();

            PVector mouseVector = new PVector(mouseX, mouseY);
            float distMouse = getDistanceTo(mouseVector);
            if(distMouse < minDistance)
            {
                PVector diff = PVector.sub(position, mouseVector);
                diff.normalize();
                steer.add(diff);
            }

            steer.limit(maxSpeed * cohesionMultiplier);
            return steer;
        }

        private float getDistanceTo(PVector other)
        {
            float distX = position.x - other.x;
            if(distX > width / 2f)
                distX -= width / 2f;

            float distY = position.y - other.y;
            if(distY > height / 2f)
                distY -= height / 2f;

            return new PVector(distX, distY).mag();
        }
    }
}
