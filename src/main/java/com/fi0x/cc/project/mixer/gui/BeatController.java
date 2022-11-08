package com.fi0x.cc.project.mixer.gui;

import com.fi0x.cc.project.mixer.MixerManager;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;
import java.util.function.Function;

public class BeatController
{
    private final PApplet parent;
    private int controlX;
    private int controlY;

    private final ArrayList<CustomButton> buttons = new ArrayList<>();

    public BeatController(PApplet parentScreen)
    {
        parent = parentScreen;

        Function<Integer, Boolean> changeBPM = v ->
        {
            MixerManager.changeBPM(v);
            return true;
        };
        buttons.add(new CustomButton(30, 30, "BPM-", changeBPM, -1));
        buttons.add(new CustomButton(110, 30, "BPM+", changeBPM, 1));

        Function<Integer, Boolean> changeNotes = v ->
        {
            MixerManager.changeNPB(v);
            return true;
        };
        buttons.add(new CustomButton(30, 85, "Notes-", changeNotes, -1));
        buttons.add(new CustomButton(110, 85, "Notes+", changeNotes, 1));
    }

    public void draw()
    {
        parent.pushMatrix();
        parent.translate(controlX, controlY);
        parent.fill(255);
        parent.textSize(12);
        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.text(MixerManager.getBPM(), 70, 30);
        parent.text(MixerManager.getNotesPerBeat(), 70, 85);

        for(CustomButton button : buttons)
            button.draw();

        parent.popMatrix();
    }

    public boolean interact(int x, int y)
    {
        for(CustomButton button : buttons)
        {
            if(button.press(x, y))
                return true;
        }
        return false;
    }

    public void changeLocation(int addedX, int addedY)
    {
        controlX += addedX;
        controlY += addedY;
    }

    private class CustomButton
    {
        private final int posX;
        private final int posY;
        private final String text;
        Function<Integer, Boolean> action;
        private final int actionValue;

        private CustomButton(int positionX, int positionY, String buttonText, Function<Integer, Boolean> runnableAction, int valueForAction)
        {
            posX = positionX;
            posY = positionY;
            text = buttonText;

            action = runnableAction;
            actionValue = valueForAction;
        }

        private void draw()
        {
            parent.pushMatrix();
            parent.translate(posX, posY);

            parent.fill(255, 0, 0);
            parent.ellipse(0, 0, 50, 50);

            parent.fill(255);
            parent.textSize(12);
            parent.textAlign(PConstants.CENTER, PConstants.CENTER);
            parent.text(text, 0, 0);

            parent.popMatrix();
        }

        private boolean press(int x, int y)
        {
            if(isAbove(x, y))
            {
                action.apply(actionValue);
                return true;
            }

            return false;
        }

        private boolean isAbove(int x, int y)
        {
            float distance = PApplet.dist(x, y, controlX + posX, controlY + posY);

            return !(distance > UIConstants.SETTINGS_ELEMENT_SIZE / 2f);
        }
    }
}
