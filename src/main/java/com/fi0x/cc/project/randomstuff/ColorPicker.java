package com.fi0x.cc.project.randomstuff;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class ColorPicker extends PApplet
{
    private final PVector SELECTOR_SIZE = new PVector(400, 225);
    private final PVector SHIFTER_SIZE = new PVector(SELECTOR_SIZE.x, 30);
    private final PVector HEX_DISPLAY_SIZE = new PVector(80, 30);

    private final PVector SELECTOR_TRANSLATION = new PVector();
    private float hueShift = 0;
    private PVector selectorDragStart;
    private PVector shifterDragStart;

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
        frameRate(120f);
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
    }

    @Override
    public void mousePressed()
    {
        if(mouseButton == RIGHT)
            return;

        if(mouseX >= 10 && mouseX <= SHIFTER_SIZE.x + 10)
        {
            if(mouseY >= 10 && mouseY <= SELECTOR_SIZE.y + 10)
            {
                selectorDragStart = new PVector(mouseX, mouseY);
            }
            else if(mouseY >= 10 + SELECTOR_SIZE.y + 10 && mouseY <= 10 + SELECTOR_SIZE.y + 10 + SHIFTER_SIZE.y)
            {
                shifterDragStart = new PVector(mouseX, mouseY);
            }
        }
    }
    @Override
    public void mouseReleased()
    {
        if(mouseButton == RIGHT)
            return;

        if(selectorDragStart != null)
        {
            PVector translation = calculateSelectorTrans();
            SELECTOR_TRANSLATION.x = (translation.x + SELECTOR_SIZE.x) % SELECTOR_SIZE.x;
            SELECTOR_TRANSLATION.y = (translation.y + SELECTOR_SIZE.y) % SELECTOR_SIZE.y;
            selectorDragStart = null;
        }
        else if(shifterDragStart != null)
        {
            hueShift = calculateHue();
            shifterDragStart = null;
        }
    }

    private void drawSelector()
    {
        //TODO: Translate colors according to transformation AND current mouseDrag
        for(int x = 0; x < SELECTOR_SIZE.x; x++)
        {
            for(int y = 0; y < SELECTOR_SIZE.y; y++)
            {
                PVector translation = calculateSelectorTrans();
                stroke(calculateHue(), (x + translation.x) % SELECTOR_SIZE.x, (SELECTOR_SIZE.y - y - translation.y + SELECTOR_SIZE.y) % SELECTOR_SIZE.y);
                point(x, y);
            }
        }

        stroke((calculateHue() + SHIFTER_SIZE.x / 2) % SHIFTER_SIZE.x, SELECTOR_SIZE.x, SELECTOR_SIZE.y);
        ellipse(SELECTOR_SIZE.x / 2, SELECTOR_SIZE.y / 2, 10, 10);
    }
    private void drawShifter()
    {
        for(int x = 0; x < SHIFTER_SIZE.x; x++)
        {
            stroke((calculateHue() + x + SHIFTER_SIZE.x / 2) % SHIFTER_SIZE.x, SELECTOR_SIZE.x, SELECTOR_SIZE.y);
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

    private float calculateHue()
    {
        float shiftedDistance = shifterDragStart == null ? 0 : shifterDragStart.x - mouseX;
        float hueShifted = hueShift + shiftedDistance;
        while(hueShifted < 0)
            hueShifted += SHIFTER_SIZE.x;
        return hueShifted % SHIFTER_SIZE.x;
    }
    private PVector calculateSelectorTrans()
    {
        float translatedXDistance = selectorDragStart == null ? 0 : selectorDragStart.x - mouseX;
        float xTranslated = SELECTOR_TRANSLATION.x + translatedXDistance;
        while(xTranslated < 0)
            xTranslated += SELECTOR_SIZE.x;

        float translatedYDistance = selectorDragStart == null ? 0 : selectorDragStart.y - mouseY;
        float yTranslated = SELECTOR_TRANSLATION.y + translatedYDistance;
        while(yTranslated < 0)
            yTranslated += SELECTOR_SIZE.y;

        return new PVector(xTranslated % SELECTOR_SIZE.x, yTranslated % SELECTOR_SIZE.y);
    }
}
