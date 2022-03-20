package com.fi0x.cc.project;

import io.fi0x.javalogger.logging.Logger;

public class LoggerManager
{
    public static void initializeTemplates()
    {
        Logger.createNewTemplate(String.valueOf(Template.INFO_WHITE), Logger.WHITE, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_GREEN), Logger.GREEN, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_YELLOW), Logger.YELLOW, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_RED), Logger.RED, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_BLUE), Logger.BLUE, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.INFO_PURPLE), Logger.PURPLE, "INF", false, false, false, true);
        Logger.createNewTemplate(String.valueOf(Template.DEBUG_INFO), Logger.CYAN, "INF", false, false, true, false);
        Logger.createNewTemplate(String.valueOf(Template.DEBUG_WARNING), Logger.YELLOW, "WRN", false, false, true, false);
        Logger.createNewTemplate(String.valueOf(Template.DEBUG_ERROR), Logger.RED, "ERR", false, false, true, false);
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
