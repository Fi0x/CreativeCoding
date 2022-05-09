package com.fi0x.cc.exercises;

import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Startup
{
    private static final Map<String, String> exerciseMap = new HashMap<>()
    {{
        put("1.1", "com.fi0x.cc.exercises.week1.Exercise1");
        put("1.3", "com.fi0x.cc.exercises.week1.Exercise3");
        put("1.3b", "com.fi0x.cc.exercises.week1.Exercise3Different");
        put("1.4", "com.fi0x.cc.exercises.week1.Exercise4");
        put("1.4b", "com.fi0x.cc.exercises.week1.Exercise4Different");
        put("1.4c", "com.fi0x.cc.exercises.week1.Exercise4Pointillism");
        put("2.1", "com.fi0x.cc.exercises.week2.Exercise1");
        put("2.2", "com.fi0x.cc.exercises.week2.Exercise2");
        put("2.3", "com.fi0x.cc.exercises.week2.Exercise3");
        put("2.4", "com.fi0x.cc.exercises.week2.Exercise4");
        put("3.1", "com.fi0x.cc.exercises.week3.Exercise1");
    }};

    public static void main(String[] args)
    {
        for(String arg : args)
        {
            if(arg.equals("-d"))
                Logger.getInstance().setDebug(true);
        }

        Logger.createNewTemplate(String.valueOf(LogTemplate.INFO_WHITE), Logger.WHITE, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(LogTemplate.INFO_GREEN), Logger.GREEN, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(LogTemplate.INFO_YELLOW), Logger.YELLOW, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(LogTemplate.INFO_RED), Logger.RED, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(LogTemplate.INFO_BLUE), Logger.BLUE, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(LogTemplate.INFO_PURPLE), Logger.PURPLE, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(LogTemplate.DEBUG_INFO), Logger.CYAN, "INF", false, false, true, false);
        Logger.createNewTemplate(String.valueOf(LogTemplate.DEBUG_WARNING), Logger.YELLOW, "WRN", false, false, true, false);
        Logger.createNewTemplate(String.valueOf(LogTemplate.DEBUG_ERROR), Logger.RED, "ERR", false, false, true, false);

        Scanner sc = new Scanner(System.in);
        Logger.log("Welcome to my Creative Coding Lecture Project!", String.valueOf(LogTemplate.INFO_WHITE));
        showMainMenu(sc);
    }

    private static void showMainMenu(Scanner sc)
    {
        StringBuilder exerciseIDs = new StringBuilder();
        for(String id : exerciseMap.keySet())
        {
            if(exerciseIDs.length() > 0)
                exerciseIDs.append(", ");
            exerciseIDs.append(id);
        }
        Logger.log("Please type in the number of the exercise you would like to see (" + exerciseIDs + "): ", String.valueOf(LogTemplate.INFO_BLUE));
        String input = sc.next().toLowerCase(Locale.ROOT).replace("a", "");

        if(exerciseMap.containsKey(input))
        {
            Logger.log("Starting exercise...", String.valueOf(LogTemplate.INFO_GREEN));
            PApplet.main(exerciseMap.get(input));
        } else
        {
            Logger.log("No valid input, please try a valid exercise id.", String.valueOf(LogTemplate.INFO_RED));
            showMainMenu(sc);
        }
    }

    public enum LogTemplate
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
