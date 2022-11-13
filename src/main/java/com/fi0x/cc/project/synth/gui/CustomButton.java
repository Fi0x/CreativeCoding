package com.fi0x.cc.project.synth.gui;

import processing.core.PApplet;
import processing.core.PConstants;

import java.util.function.Function;

public class CustomButton
{
    private final PApplet parent;
    private int x;
    private int y;
    private final int w;
    private final int h;
    private String text;
    Function<Integer, Boolean> action;

    public CustomButton(String buttonText, int xPos, int yPos, int width, int height, Function<Integer, Boolean> runnableAction, PApplet parentScreen)
    {
        text = buttonText;
        x = xPos;
        y = yPos;
        w = width;
        h = height;
        action = runnableAction;
        parent = parentScreen;
    }

    public void draw()
    {
        parent.fill(61, 28, 145);
        parent.rect(x, y, w, h);
        parent.fill(255);
        parent.textSize(13);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text(text, x + w / 2f, y + h / 2f);
    }

    private boolean isAbove(int xCheck, int yCheck)
    {
        if(xCheck < x || xCheck > x + w)
            return false;

        return yCheck >= y && yCheck <= y + h;
    }
    public boolean interact(int xCheck, int yCheck)
    {
        if(isAbove(xCheck, yCheck))
        {
            action.apply(0);
            return true;
        }
        return false;
    }

    public void updatePosition(int xPos, int yPos)
    {
        x = xPos;
        y = yPos;
    }
    public void updateText(String newText)
    {
        text = newText;
    }
}
