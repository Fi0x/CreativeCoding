package com.fi0x.cc.exercises.week7;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

public class Exercise1 extends PApplet
{
    private ArrayList<Particle> particles = new ArrayList<>();
    private final int maxParticles = 100;
    private final int particleSize = 50;
    private final float maxSpeed = 5;
    private final PVector hsv = new PVector(0, 0, 0);

    @Override
    public void settings()
    {
        size(500, 500);
        smooth(4);
    }
    @Override
    public void exitActual()
    {
    }
    @Override
    public void setup()
    {
        frameRate(60f);
        noFill();
        randomSeed(90284570918234345L);
        colorMode(HSB, 360, 100, 100);
    }

    @Override
    public void draw()
    {
        background(0);
        hsv.add(1, 0, 0);
        if(hsv.x > 360)
            hsv.x = 0;

        if(particles.size() < maxParticles)
            particles.add(new Particle(mouseX, mouseY));

        for(int i = 0; i < particles.size(); i++)
        {
            Particle p = particles.get(i);
            p.update();
            if(p.alpha < 0)
            {
                particles.remove(p);
                i--;
            }
            else
                p.display();
        }
    }

    private class Particle
    {
        private final PImage img = loadImage("images/blackSmokeParticle.png");
        private float x;
        private float y;
        private float alpha = 100;
        private final PVector motion;

        private Particle(float x, float y)
        {
            this.x = x;
            this.y = y;
            motion = new PVector(random(-maxSpeed, maxSpeed), -random(maxSpeed / 2f, maxSpeed));
        }

        void update()
        {
            alpha -= maxSpeed;
            motion.add(random(-maxSpeed / 4f, maxSpeed / 4f), -random(maxSpeed / 2f, maxSpeed));
            motion.limit(maxSpeed);
            x += motion.x;
            y += motion.y;
        }

        void display()
        {
            image(img, x - particleSize / 2f, y - particleSize / 2f, particleSize, particleSize);
            tint(hsv.x, 255, 255, alpha);
        }
    }
}
