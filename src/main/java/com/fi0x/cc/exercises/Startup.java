package com.fi0x.cc.exercises;

import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;

import java.util.Scanner;

public class Startup
{
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
        Logger.log("Please type in the number of the exercise you would like to see(1-8): ", String.valueOf(LogTemplate.INFO_BLUE));
        String input = sc.next();

        switch(input)
        {
            case "1":
                Logger.log("Starting exercise 1-1", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.exercises.week1.Exercise1");
                break;
            case "2":
                Logger.log("Starting exercise 1-3", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.exercises.week1.Exercise3");
                break;
            case "3":
                Logger.log("Starting exercise 1-3 variant", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.exercises.week1.Exercise3Different");
                break;
            case "4":
                Logger.log("Starting exercise 1-4", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.exercises.week1.Exercise4");
                break;
            case "5":
                Logger.log("Starting exercise 1-4 variant", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.exercises.week1.Exercise4Different");
                break;
            case "6":
                Logger.log("Starting exercise 1-4 variant", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.exercises.week1.Exercise4Pointillism");
                break;
            case "7":
                Logger.log("Starting exercise 2-1", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.exercises.week2.Exercise1");
                break;
            case "8":
                Logger.log("Nothing here yet", String.valueOf(LogTemplate.INFO_GREEN));
                break;
            default:
                Logger.log("No valid input, please try a valid number(1-8): ", String.valueOf(LogTemplate.INFO_RED));
                showMainMenu(sc);
                break;
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
