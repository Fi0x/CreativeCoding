package com.fi0x.cc.exercises.week5;

import processing.core.PApplet;

import java.util.ArrayList;

public class Exercise1FractalTree extends PApplet
{
    private final int maxLevel = 8;
    private final int averageSubBranches = 3;
    private final float rootLength = 70;
    private final float rootThickness = 5;
    private final float lengthMultiplier = 0.9f;
    private final float thicknessMultiplier = 0.8f;

    private Branch rootBranch;

    @Override
    public void settings()
    {
        size(1000, 500);
    }
    @Override
    public void setup()
    {
        frameRate(1);
        fill(255);
        stroke(255);

    }

    @Override
    public void draw()
    {
        background(0);
        translate(width / 2f, height);
        scale(1, -1);

        rootBranch = new Branch(rootLength, rootThickness, 0);
        rootBranch.createSubBranches((int) (random(2) + averageSubBranches - 1));
        rootBranch.drawBranchWithSubBranches();
    }

    private class Branch
    {
        private final float length;
        private final float thickness;
        private final int treeLevel;
        private final ArrayList<Branch> childBranches = new ArrayList<>();

        private Branch(float branchLength, float branchThickness, int level)
        {
            length = branchLength;
            treeLevel = level;
            thickness = branchThickness;
        }
        private void createSubBranches(int count)
        {
            if(treeLevel < maxLevel)
            {
                for(int i = 0; i < count; i++)
                {
                    Branch b = new Branch(length * lengthMultiplier, thickness * thicknessMultiplier, treeLevel + 1);
                    b.createSubBranches((int) (random(2) + averageSubBranches - 1));
                    childBranches.add(b);
                }
            }
        }

        private void drawBranchWithSubBranches()
        {
            pushMatrix();
            rotate(radians(random(40) - 20));
            strokeWeight(thickness);
            line(0, 0, 0, length);

            translate(0, length);
            for(Branch b : childBranches)
                b.drawBranchWithSubBranches();

            popMatrix();
        }
    }
}
