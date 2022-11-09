package com.fi0x.cc;

import com.fi0x.cc.project.mixer.gui.MainMixerWindow;
import com.fi0x.cc.project.randomstuff.ColorPicker;
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
