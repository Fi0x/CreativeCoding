package com.fi0x.cc.project.synth.gui;

import com.fi0x.cc.project.synth.SynthManager;
import com.fi0x.cc.project.synth.synthesizers.AbstractSynth;
import com.fi0x.cc.project.synth.synthesizers.ISynthesizer;
import processing.core.PApplet;

import java.util.function.Function;

public class SynthUI
{
    private final ISynthesizer linkedSynth;
    private final AbstractSoundVisualizer visualizer;

    private int x;
    private int y;
    private int xSize;
    private int ySize;

    private boolean allowInstrumentChanges = true;
    private final CustomButton[] buttons = new CustomButton[2];

    private final PApplet parentScreen;

    private boolean playStatus;
    private boolean muteStatus;

    public SynthUI(PApplet parent, int xPos, int yPos, int w, int h) throws IllegalStateException
    {
        linkedSynth = SynthManager.getNextSynth();
        if(linkedSynth == null)
            throw new IllegalStateException("No Synths loaded that could be linked.");

        linkedSynth.linkUI(this);
        if(Math.random() > 0.5)
            visualizer = new SoundVisualizer2(parent, linkedSynth, w / 2, h / 2);
        else
            visualizer = new SoundVisualizer(parent, linkedSynth, w / 2, h / 2);

        if(((AbstractSynth) linkedSynth).channelNumber == 9)
        {
            allowInstrumentChanges = false;
        }

        x = xPos;
        y = yPos;
        xSize = w;
        ySize = h;

        parentScreen = parent;
        addButtons();
    }

    public void display()
    {
        parentScreen.pushMatrix();
        parentScreen.translate(x, y);

        parentScreen.fill(100);
        parentScreen.rect(5, 5, xSize - 10, ySize - 10);
        parentScreen.textSize(14);
        parentScreen.fill(0);
        parentScreen.text("Channel: " + ((AbstractSynth) linkedSynth).channelNumber, 10, 45, 100, 30);

        parentScreen.text("Playing", 10, 85, 75, 20);
        parentScreen.text("Muted", 10, 110, 75, 20);

        parentScreen.translate((float) xSize / 2 - 10, (float) ySize / 2 - 10);
        visualizer.display();

        parentScreen.popMatrix();

        if(allowInstrumentChanges)
        {
            for(CustomButton button : buttons)
                button.draw();
        }
    }
    public void updateDisplay()
    {
        parentScreen.pushMatrix();
        parentScreen.translate(x, y);

        parentScreen.textSize(14);
        parentScreen.fill(0);
        parentScreen.text(linkedSynth.getInstrumentName(), (float) xSize / 2 - 100, 10, 200, 30);

        if(playStatus)
            parentScreen.fill(255, 0, 0);
        else
            parentScreen.fill(0);
        parentScreen.ellipse(100, 95, 20, 20);
        if(muteStatus)
            parentScreen.fill(255, 0, 0);
        else
            parentScreen.fill(0);
        parentScreen.ellipse(100, 120, 20, 20);

        parentScreen.translate((float) xSize / 2 - 10, (float) ySize / 2 - 10);
        visualizer.display();

        parentScreen.popMatrix();
    }
    public void updateSize(int posX, int posY, int width, int height)
    {
        x = posX;
        y = posY;
        xSize = width;
        ySize = height;

        visualizer.updateSize(width / 2, height / 2);

        if(allowInstrumentChanges)
            updateButtonPositions();
    }
    public void noteTriggered(boolean turnedOn)
    {
        playStatus = turnedOn;
    }
    public void setMute(boolean isMuted)
    {
        muteStatus = isMuted;
    }

    public boolean buttonClicked()
    {
        if(allowInstrumentChanges)
        {
            for(CustomButton button : buttons)
            {
                if(button.interact(parentScreen.mouseX, parentScreen.mouseY))
                    return true;
            }
        }

        return false;
    }

    private void addButtons()
    {
        if(allowInstrumentChanges)
        {
            Function<Integer, Boolean> previousSynth = v ->
            {
                linkedSynth.previousInstrument();
                return true;
            };
            buttons[0] = new CustomButton("Previous Synth", x + 10, y + 10, 100, 30, previousSynth, parentScreen);

            Function<Integer, Boolean> nextSynth = v ->
            {
                linkedSynth.nextInstrument();
                return true;
            };
            buttons[1] = new CustomButton("Next Synth", x + xSize -110, y + 10, 100, 30, nextSynth, parentScreen);

            updateButtonPositions();
        }
    }
    private void updateButtonPositions()
    {
        buttons[0].updatePosition(x + 10,  y + 10);
        buttons[1].updatePosition(x + xSize -110, y + 10);
    }
}
