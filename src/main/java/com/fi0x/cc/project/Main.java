package com.fi0x.cc.project;

import io.fi0x.javalogger.logging.Logger;
import processing.core.PApplet;

public class Main
{
    public static void main(String[] args)
    {
        for(String arg : args)
        {
            if(arg.equals("-d"))
                Logger.getInstance().setDebug(true);
        }

        LoggerManager.initializeTemplates();
        Logger.getInstance().setSmallLog(true);

        PApplet.main("com.fi0x.cc.project.gui.MainWindow");
    }
}
