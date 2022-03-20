package com.fi0x.cc.project;

import io.fi0x.javalogger.logging.Logger;

public class LoggerManager
{
    public static void initializeTemplates()
    {
        Logger.createNewTemplate(String.valueOf(Template.DEBUG_INFO), Logger.WHITE, "INF", false, false, true, false);
        Logger.createNewTemplate(String.valueOf(Template.DEBUG_WARNING), Logger.YELLOW, "WRN", false, false, true, false);
        Logger.createNewTemplate(String.valueOf(Template.DEBUG_ERROR), Logger.RED, "ERR", false, false, true, false);
    }

    public enum Template
    {
        DEBUG_INFO,
        DEBUG_WARNING,
        DEBUG_ERROR
    }
}
