package com.fi0x.cc.exercises.week5;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Exercise1FractalTree extends PApplet
{
    private int maxLevel = 10;
    private int currentLevel = 0;
    private final ArrayList<Branch> endBranches = new ArrayList<>();

    @Override
    public void settings()
    {
        size(500, 500);
    }
    @Override
    public void setup()
    {
        frameRate(1);
        fill(255);
        stroke(255);
        background(0);

        PVector start = new PVector(0, 0);
        PVector end = new PVector(0, -50);
        endBranches.add(new Branch(start, end));
    }

    @Override
    public void draw()
    {
        translate(width / 2f, height);

        for(int i = endBranches.size() - 1; i >= 0; i--)
        {
            Branch b = endBranches.get(i);
            line(b.start.x, b.start.y, b.end.x, b.end.y);

            for(int j = 0; j < random(2) + 1; j++)
            {

            }

            endBranches.remove(b);
        }
    }

    private class Branch
    {
        private final PVector start;
        private final PVector end;

        private Branch(PVector from, PVector to)
        {
            start = from;
            end = to;
        }
    }
}
