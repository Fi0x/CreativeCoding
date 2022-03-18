package com.fi0x.cc;

import com.fi0x.cc.project.synth.SynthPlayer;
import io.fi0x.javalogger.LogSettings;
import io.fi0x.javalogger.Logger;
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

        LogSettings.createNewTemplate(String.valueOf(LogTemplate.INFO_WHITE), LogSettings.WHITE, Logger.LEVEL.INF, false, false, false);
        LogSettings.createNewTemplate(String.valueOf(LogTemplate.INFO_GREEN), LogSettings.GREEN, Logger.LEVEL.INF, false, false, false);
        LogSettings.createNewTemplate(String.valueOf(LogTemplate.INFO_YELLOW), LogSettings.YELLOW, Logger.LEVEL.INF, false, false, false);
        LogSettings.createNewTemplate(String.valueOf(LogTemplate.INFO_RED), LogSettings.RED, Logger.LEVEL.INF, false, false, false);
        LogSettings.createNewTemplate(String.valueOf(LogTemplate.INFO_BLUE), LogSettings.BLUE, Logger.LEVEL.INF, false, false, false);
        LogSettings.createNewTemplate(String.valueOf(LogTemplate.DEBUG_INFO), LogSettings.CYAN, Logger.LEVEL.INF, false, true, false);
        LogSettings.createNewTemplate(String.valueOf(LogTemplate.DEBUG_WARNING), LogSettings.YELLOW, Logger.LEVEL.WRN, false, true, false);
        LogSettings.createNewTemplate(String.valueOf(LogTemplate.DEBUG_ERROR), LogSettings.RED, Logger.LEVEL.ERR, false, true, false);

        Scanner sc = new Scanner(System.in);
        LogSettings.getLOGFromTemplate("Welcome to my Creative Coding Lecture Project!", String.valueOf(LogTemplate.INFO_WHITE));
        showMainMenu(sc);
    }

    private static void showMainMenu(Scanner sc)
    {
        LogSettings.getLOGFromTemplate("Please type in the number of the exercise you would like to see(1-8): ", String.valueOf(LogTemplate.INFO_BLUE));
        String input = sc.next();

        switch(input)
        {
            case "1":
                LogSettings.getLOGFromTemplate("Starting exercise 1-1", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.week1.Exercise1");
                break;
            case "2":
                LogSettings.getLOGFromTemplate("Starting exercise 1-3", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.week1.Exercise3");
                break;
            case "3":
                LogSettings.getLOGFromTemplate("Starting exercise 1-3 variant", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.week1.Exercise3Different");
                break;
            case "4":
                LogSettings.getLOGFromTemplate("Starting exercise 1-4", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.week1.Exercise4");
                break;
            case "5":
                LogSettings.getLOGFromTemplate("Starting exercise 1-4 variant", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.week1.Exercise4Different");
                break;
            case "6":
                LogSettings.getLOGFromTemplate("Starting exercise 1-4 variant", String.valueOf(LogTemplate.INFO_GREEN));
                PApplet.main("com.fi0x.cc.week1.Exercise4Pointillism");
                break;
            case "7":
                LogSettings.getLOGFromTemplate("Starting Synthesizer", String.valueOf(LogTemplate.INFO_GREEN));
                SynthPlayer.getInstance().startListening();
                break;
            case "8":
                LogSettings.getLOGFromTemplate("Nothing here yet", String.valueOf(LogTemplate.INFO_GREEN));
                break;
            default:
                LogSettings.getLOGFromTemplate("No valid input, please try a valid number(1-8): ", String.valueOf(LogTemplate.INFO_RED));
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
        DEBUG_INFO,
        DEBUG_WARNING,
        DEBUG_ERROR
    }
}
