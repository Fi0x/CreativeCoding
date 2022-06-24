package com.fi0x.cc.exercises;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;

import java.util.ArrayList;

public class MenuScreen extends PApplet
{
    private ControlP5 controller;
    private final int buttonWidth = 50;
    private final int buttonHeight = 30;
    private final int buttonDistance = 20;
    private final int buttonColumns = 6;
    private final int buttonRows = 4;
    private static final ArrayList<Entry> exercises = new ArrayList<>();

    public void fillExerciseList()
    {
        exercises.add(new Entry("1.1", "com.fi0x.cc.exercises.week1.Exercise1"));
        exercises.add(new Entry("1.3", "com.fi0x.cc.exercises.week1.Exercise3"));
        exercises.add(new Entry("1.3b", "com.fi0x.cc.exercises.week1.Exercise3Different"));
        exercises.add(new Entry("1.4", "com.fi0x.cc.exercises.week1.Exercise4"));
        exercises.add(new Entry("1.4b", "com.fi0x.cc.exercises.week1.Exercise4Different"));
        exercises.add(new Entry("1.4c", "com.fi0x.cc.exercises.week1.Exercise4Pointillism"));
        exercises.add(new Entry("2.1", "com.fi0x.cc.exercises.week2.Exercise1"));
        exercises.add(new Entry("2.2", "com.fi0x.cc.exercises.week2.Exercise2"));
        exercises.add(new Entry("2.3", "com.fi0x.cc.exercises.week2.Exercise3"));
        exercises.add(new Entry("2.4", "com.fi0x.cc.exercises.week2.Exercise4"));
        exercises.add(new Entry("3.1", "com.fi0x.cc.exercises.week3.Exercise1"));
        exercises.add(new Entry("3.2", "com.fi0x.cc.exercises.week3.Exercise2"));
        exercises.add(new Entry("3.2b", "com.fi0x.cc.exercises.week3.Exercise2Variant"));
        exercises.add(new Entry("3.2c", "com.fi0x.cc.exercises.week3.Exercise2WithoutScale"));
        exercises.add(new Entry("3.3", "com.fi0x.cc.exercises.week3.Exercise3"));
        exercises.add(new Entry("4.1", "com.fi0x.cc.exercises.week4.Exercise1"));
        exercises.add(new Entry("4.2", "com.fi0x.cc.exercises.week4.Exercise2"));
        exercises.add(new Entry("5.1", "com.fi0x.cc.exercises.week5.Exercise1"));
        exercises.add(new Entry("5.1b", "com.fi0x.cc.exercises.week5.Exercise1Test"));
        exercises.add(new Entry("5.1c", "com.fi0x.cc.exercises.week5.Exercise1FractalTree"));
        exercises.add(new Entry("5.2", "com.fi0x.cc.exercises.week5.Exercise2"));
        exercises.add(new Entry("5.2b", "com.fi0x.cc.exercises.week5.Exercise2MinDistance"));
        exercises.add(new Entry("5.2c", "com.fi0x.cc.exercises.week5.Exercise2Lines"));
        exercises.add(new Entry("5.3", "com.fi0x.cc.exercises.week5.Exercise3"));
    }

    @Override
    public void settings()
    {
        int w = buttonDistance + (buttonWidth + buttonDistance) * buttonColumns;
        int h = buttonDistance + (buttonHeight + buttonDistance) * buttonRows;
        size(w, h);
    }
    @Override
    public void setup()
    {
        frameRate(60);
        fill(255);
        noStroke();

        controller = new ControlP5(this);
        fillExerciseList();
        createControls();
    }

    @Override
    public void draw()
    {
    }

    public void controlEvent(ControlEvent event)
    {
        if(!event.isController() || controller == null)
            return;

        for(Entry e : exercises)
        {
            if(event.getController() == controller.getController(e.name))
            {
                if(e.called)
                    PApplet.main(e.classLink);
                else
                    e.called = true;
                break;
            }
        }
    }

    private void createControls()
    {
        for(int x = 0, y = 0; y < buttonRows && x + y * buttonColumns < exercises.size(); y++)
        {
            for(; x < buttonColumns && x + y * buttonColumns < exercises.size(); x++)
            {
                Entry e = exercises.get(x + y * buttonColumns);

                controller.addButton(e.name)
                        .setSize(buttonWidth, buttonHeight)
                        .setPosition(buttonDistance + x * (buttonWidth + buttonDistance), buttonDistance + y * (buttonHeight + buttonDistance))
                        .setValue(0);
            }
            x = 0;
        }
    }

    private class Entry
    {
        private final String name;
        private final String classLink;

        private boolean called;

        private Entry(String labeledName, String link)
        {
            name = labeledName;
            classLink = link;
        }
    }
}
