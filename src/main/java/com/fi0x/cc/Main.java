package com.fi0x.cc;

import io.fi0x.javaguimenu.GUIWindow;
import io.fi0x.javaguimenu.elements.AbstractElement;
import io.fi0x.javaguimenu.elements.Listener;
import io.fi0x.javaguimenu.elements.RegularButton;
import io.fi0x.javaguimenu.layouts.LayoutTypes;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;

import java.util.ArrayList;

public class Main
{
    private static final ArrayList<Entry> exercises = new ArrayList<>();

    private static final int COLS = 5;
    private static final int ROWS = 7;

    public static void main(String[] args)
    {
        setupLogger(args);
        createMenuWindow(args);
    }

    private static void setupLogger(String[] args)
    {
        for(String arg : args)
        {
            if(arg.equals("-d"))
                Logger.getInstance().setDebug(true);
            if(arg.equals("-v"))
                Logger.getInstance().setVerbose(true);
        }

        Logger.getInstance().setSmallLog(true);

        Logger.createNewTemplate(String.valueOf(Template.INFO_WHITE), Logger.WHITE, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_GREEN), Logger.GREEN, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_YELLOW), Logger.YELLOW, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_RED), Logger.RED, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_BLUE), Logger.BLUE, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_PURPLE), Logger.PURPLE, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.DEBUG_INFO), Logger.CYAN, "INF", false, false, true, false);
        Logger.createNewTemplate(String.valueOf(Template.DEBUG_WARNING), Logger.YELLOW, "WRN", false, false, true, false);
        Logger.createNewTemplate(String.valueOf(Template.DEBUG_ERROR), Logger.RED, "ERR", false, false, true, false);


        Logger.log("Welcome to my Creative Coding Lecture Project!", String.valueOf(Template.INFO_WHITE));
    }
    private static void createMenuWindow(String[] args)
    {
        GUIWindow.setTitle("Creative Coding Menu");

        GUIWindow.setColumns(COLS);
        GUIWindow.setRows(ROWS);
        GUIWindow.setElementSpacing(false);
        GUIWindow.setLayout(LayoutTypes.Grid);

        new Main().fillExerciseList();
        createButtons();

        GUIWindow.start(args);

    }
    private static void createButtons()
    {
        RegularButton btnMixer = RegularButton.create(0, 0);
        btnMixer.setText("Mixer");
        btnMixer.addListener(sender -> PApplet.main("com.fi0x.cc.project.gui.mixer.MainMixerWindow"));
        GUIWindow.addElement(btnMixer);

        RegularButton btnSynth = RegularButton.create(1, 0);
        btnSynth.setText("Synthesizer");
        btnSynth.addListener(sender -> PApplet.main("com.fi0x.cc.project.gui.synth.MainSynthWindow"));
        GUIWindow.addElement(btnSynth);

        int existingButtons = 2;

        for(int x = existingButtons, y = 0; y < ROWS && x + y * COLS < exercises.size() + existingButtons; y++)
        {
            for(; x < COLS && x + y * COLS < exercises.size() + existingButtons; x++)
            {
                final Entry e = exercises.get(x + y * COLS - existingButtons);

                System.out.println(e.name + ":\tat: " + x + ", " + y);

                RegularButton btn = RegularButton.create(x, y);
                btn.setText(e.name);
                btn.addListener(new Listener()
                {
                    final String link = e.classLink;
                    @Override
                    public void trigger(AbstractElement sender)
                    {
                        PApplet.main(link);
                    }
                });
                GUIWindow.addElement(btn);
            }
            x = 0;
        }
    }

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
        exercises.add(new Entry("6.1", "com.fi0x.cc.exercises.week6.Exercise1"));
        exercises.add(new Entry("6.1b", "com.fi0x.cc.exercises.week6.Exercise1Spikes"));
        exercises.add(new Entry("6.1c", "com.fi0x.cc.exercises.week6.Exercise1Stars"));
        exercises.add(new Entry("7.1", "com.fi0x.cc.exercises.week7.Exercise1"));
        exercises.add(new Entry("7.1b", "com.fi0x.cc.exercises.week7.Exercise1Wind"));
        exercises.add(new Entry("8.1", "com.fi0x.cc.exercises.week8.Exercise1"));
        exercises.add(new Entry("8.1b", "com.fi0x.cc.exercises.week8.Exercise1Mouse"));
        exercises.add(new Entry("8.1c", "com.fi0x.cc.exercises.week8.Exercise1Lines"));
    }

    public enum Template
    {
        INFO_WHITE,
        INFO_GREEN,
        INFO_YELLOW,
        INFO_RED,
        INFO_BLUE,
        INFO_PURPLE,
        DEBUG_INFO,
        DEBUG_WARNING,
        DEBUG_ERROR
    }

    private class Entry
    {
        private final String name;
        private final String classLink;

        private Entry(String labeledName, String link)
        {
            name = labeledName;
            classLink = link;
        }
    }
}
