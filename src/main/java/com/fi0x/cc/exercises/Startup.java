package com.fi0x.cc.exercises;

import controlP5.ControlP5;
import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Startup extends PApplet
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

        PApplet.main("com.fi0x.cc.exercises.MenuScreen");
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
