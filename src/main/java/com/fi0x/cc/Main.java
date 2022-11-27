package com.fi0x.cc;

import com.fi0x.cc.exercises.week1.*;
import com.fi0x.cc.project.mixer.gui.MainMixerWindow;
import com.fi0x.cc.project.randomstuff.ColorPicker;
import com.fi0x.cc.project.randomstuff.Mandelbrot;
import com.fi0x.cc.project.synth.gui.MainSynthWindow;
import io.fi0x.javaguimenu.GUIWindow;
import io.fi0x.javaguimenu.elements.AbstractElement;
import io.fi0x.javaguimenu.elements.Listener;
import io.fi0x.javaguimenu.elements.PriorityButton;
import io.fi0x.javaguimenu.elements.RegularButton;
import io.fi0x.javaguimenu.layouts.LayoutTypes;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main
{
    private static final SortedMap<String, String> exercises = new TreeMap<>();

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

        GUIWindow.setWindowIcon("images/logo.png");
        GUIWindow.setCssFile("css/menu_layout.css");
        GUIWindow.setColumns(COLS);
        GUIWindow.setRows(ROWS);
        GUIWindow.setElementSpacing(true);
        GUIWindow.setLayout(LayoutTypes.Grid);
        GUIWindow.showGridLanes(true);
        GUIWindow.setStopAllOnExit(true);

        loadClasses("com/fi0x/cc/exercises");
        Logger.log(exercises.size() + " Exercise-Classes loaded.", String.valueOf(Template.INFO_GREEN));
        createButtons();

        GUIWindow.start(args);
    }
    private static void createButtons()
    {
        PriorityButton btnMixer = new PriorityButton();
        btnMixer.setText("Projekt\nMixer");
        btnMixer.addListener(sender -> PApplet.main(MainMixerWindow.class.getName()));
        GUIWindow.addElement(btnMixer);

        PriorityButton btnSynth = new PriorityButton();
        btnSynth.setText("Projekt\nSynthesizer");
        btnSynth.addListener(sender -> PApplet.main(MainSynthWindow.class.getName()));
        GUIWindow.addElement(btnSynth);

        PriorityButton btnColorPick = new PriorityButton();
        btnColorPick.setText("Projekt\nColor Picker");
        btnColorPick.addListener(sender -> PApplet.main(ColorPicker.class.getName()));
        GUIWindow.addElement(btnColorPick);

        PriorityButton btnMandelbrot = new PriorityButton();
        btnMandelbrot.setText("Projekt\nMandelbrot");
        btnMandelbrot.addListener(sender -> PApplet.main(Mandelbrot.class.getName()));
        GUIWindow.addElement(btnMandelbrot);

        for(Map.Entry<String, String> e : exercises.entrySet())
        {
            RegularButton btn = new RegularButton();
            btn.setText(e.getKey());
            btn.addListener(new Listener()
            {
                final String link = e.getValue();
                @Override
                public void trigger(AbstractElement sender)
                {
                    PApplet.main(link);
                }
            });
            GUIWindow.addElement(btn);
        }
    }
    private static void loadClasses(String packageName)
    {
        //TODO: Use for .exe version
//        exercises.put("Week 1\nExercise1", Exercise1.class.getName());
//        exercises.put("Week 1\nExercise3", Exercise3.class.getName());
//        exercises.put("Week 1\nExercise3Different", Exercise3Different.class.getName());
//        exercises.put("Week 1\nExercise4", Exercise4.class.getName());
//        exercises.put("Week 1\nExercise4Different", Exercise4Different.class.getName());
//        exercises.put("Week 1\nExercise4Pointillism", Exercise4Pointillism.class.getName());
//        exercises.put("Week 2\nExercise1", com.fi0x.cc.exercises.week2.Exercise1.class.getName());
//        exercises.put("Week 2\nExercise2", com.fi0x.cc.exercises.week2.Exercise2.class.getName());
//        exercises.put("Week 2\nExercise3", com.fi0x.cc.exercises.week2.Exercise3.class.getName());
//        exercises.put("Week 2\nExercise4", com.fi0x.cc.exercises.week2.Exercise4.class.getName());
//        exercises.put("Week 3\nExercise1", com.fi0x.cc.exercises.week3.Exercise1.class.getName());
//        exercises.put("Week 3\nExercise2", com.fi0x.cc.exercises.week3.Exercise2.class.getName());
//        exercises.put("Week 3\nExercise2Variant", com.fi0x.cc.exercises.week3.Exercise2Variant.class.getName());
//        exercises.put("Week 3\nExercise2WithoutScale", com.fi0x.cc.exercises.week3.Exercise2WithoutScale.class.getName());
//        exercises.put("Week 3\nExercise3", com.fi0x.cc.exercises.week3.Exercise3.class.getName());
//        exercises.put("Week 4\nExercise1", com.fi0x.cc.exercises.week4.Exercise1.class.getName());
//        exercises.put("Week 4\nExercise2", com.fi0x.cc.exercises.week4.Exercise2.class.getName());
//        exercises.put("Week 5\nExercise1", com.fi0x.cc.exercises.week5.Exercise1.class.getName());
//        exercises.put("Week 5\nExercise1FractalTree", com.fi0x.cc.exercises.week5.Exercise1FractalTree.class.getName());
//        exercises.put("Week 5\nExercise1Test", com.fi0x.cc.exercises.week5.Exercise1Test.class.getName());
//        exercises.put("Week 5\nExercise2", com.fi0x.cc.exercises.week5.Exercise2.class.getName());
//        exercises.put("Week 5\nExercise2Lines", com.fi0x.cc.exercises.week5.Exercise2Lines.class.getName());
//        exercises.put("Week 5\nExercise2MinDistance", com.fi0x.cc.exercises.week5.Exercise2MinDistance.class.getName());
//        exercises.put("Week 5\nExercise3", com.fi0x.cc.exercises.week5.Exercise3.class.getName());
//        exercises.put("Week 6\nExercise1", com.fi0x.cc.exercises.week6.Exercise1.class.getName());
//        exercises.put("Week 6\nExercise1Spikes", com.fi0x.cc.exercises.week6.Exercise1Spikes.class.getName());
//        exercises.put("Week 6\nExercise1Stars", com.fi0x.cc.exercises.week6.Exercise1Stars.class.getName());
//        exercises.put("Week 7\nExercise1", com.fi0x.cc.exercises.week7.Exercise1.class.getName());
//        exercises.put("Week 7\nExercise1Wind", com.fi0x.cc.exercises.week7.Exercise1Wind.class.getName());
//        exercises.put("Week 8\nExercise1", com.fi0x.cc.exercises.week8.Exercise1.class.getName());
//        exercises.put("Week 8\nExercise1Lines", com.fi0x.cc.exercises.week8.Exercise1Lines.class.getName());
//        exercises.put("Week 8\nExercise1Mouse", com.fi0x.cc.exercises.week8.Exercise1Mouse.class.getName());



        InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName);
        assert input != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        Set<String> entries = reader.lines().collect(Collectors.toSet());

        for(String e : entries)
        {
            if(e.contains("$"))
                continue;

            if(e.endsWith(".class"))
            {
                String[] path = packageName.split("/");
                String labelName = path[path.length - 1] + "\n" + e.replace(".class", "");
                String classPath = packageName + "/" + e.replace(".class", "");
                exercises.put(labelName, classPath.replace("/", "."));
            }
            else
                loadClasses(packageName + "/" + e);
        }
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
}
