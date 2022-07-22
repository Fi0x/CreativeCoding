package com.fi0x.cc.project.randomstuff;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class ColorPicker extends PApplet
{
    private final PVector SELECTOR_SIZE = new PVector(400, 225);
    private final PVector SHIFTER_SIZE = new PVector(SELECTOR_SIZE.x, 30);
    private final PVector HEX_DISPLAY_SIZE = new PVector(80, 30);

    private float hueShift = 0;

    @Override
    public void settings()
    {
        size((int) (SELECTOR_SIZE.x + 20), (int) (SELECTOR_SIZE.y + 20 + SHIFTER_SIZE.y + 10 + HEX_DISPLAY_SIZE.y + 10));
    }
    @Override
    public void exitActual()
    {
    }
    @Override
    public void setup()
    {
        frameRate(60f);
        fill(0,0);
        background(255);
        colorMode(HSB, SHIFTER_SIZE.x, SELECTOR_SIZE.x, SELECTOR_SIZE.y, 1);
    }

    @Override
    public void draw()
    {
        translate(10, 10);
        drawSelector();

        translate(0, SELECTOR_SIZE.y + 10);
        drawShifter();

        translate(SHIFTER_SIZE.x / 2 - HEX_DISPLAY_SIZE.x / 2, SHIFTER_SIZE.y + 10);
        drawHexCode();

        hueShift++;
        hueShift %= SHIFTER_SIZE.x;

        //TODO: Create a HSV color picker with an endless shift-stripe and endless HV-screen
    }

    private void drawSelector()
    {
        for(int x = 0; x < SELECTOR_SIZE.x; x++)
        {
            for(int y = 0; y < SELECTOR_SIZE.y; y++)
            {
                stroke(hueShift, x, SELECTOR_SIZE.y - y);
                point(x, y);
            }
        }

        stroke((hueShift + SHIFTER_SIZE.x / 2) % SHIFTER_SIZE.x, SELECTOR_SIZE.x, SELECTOR_SIZE.y);
        ellipse(SELECTOR_SIZE.x / 2, SELECTOR_SIZE.y / 2, 10, 10);
    }
    private void drawShifter()
    {
        for(int x = 0; x < SHIFTER_SIZE.x; x++)
        {
            stroke((hueShift + x + SHIFTER_SIZE.x / 2) % SHIFTER_SIZE.x, SELECTOR_SIZE.x, SELECTOR_SIZE.y);
            line(x, 0, x, SHIFTER_SIZE.y);
        }

        stroke(0, 0, 0);
        line(SHIFTER_SIZE.x / 2, 0, SHIFTER_SIZE.x / 2, SHIFTER_SIZE.y);
    }

    private void drawHexCode()
    {
        loadPixels();
        int selectedColor = pixels[(int) (SELECTOR_SIZE.x / 2 + SELECTOR_SIZE.y / 2 * SELECTOR_SIZE.x)];
        fill(0, 0, SELECTOR_SIZE.y);
        rect(0, 0, HEX_DISPLAY_SIZE.x, HEX_DISPLAY_SIZE.y);
        fill(0, 0, 0);
        textAlign(PConstants.CENTER, PConstants.CENTER);
        text(hex(selectedColor), HEX_DISPLAY_SIZE.x / 2, HEX_DISPLAY_SIZE.y / 2);
        fill(0, 0);
    }
}
