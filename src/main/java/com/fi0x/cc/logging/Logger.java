package com.fi0x.cc.logging;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Logger
{
    private static final File localStorage = new File(System.getenv("APPDATA") + File.separator + "ComputerGrafik");
    private static final File logFolder = new File(localStorage.getPath() + File.separator + "Logs");
    private static final File errorFile = new File(logFolder.getPath() + File.separator + getLogfileName() + ".log");

    private static final String RESET = "\u001B[0m";
    private static final String WHITE = "\u001B[37m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    public static void INFO(String text)
    {
        log(0, LEVEL.INF, text, null);
    }
    public static void WARNING(String text)
    {
        log(0, LEVEL.WRN, text, null);
    }
    public static void WARNING(String text, Exception e)
    {
        log(0, LEVEL.WRN, text, e);
    }
    public static void WARNING(int code, String text)
    {
        log(code, LEVEL.WRN, text, null);
    }
    public static void WARNING(int code, String text, Exception e)
    {
        log(code, LEVEL.WRN, text, e);
    }
    public static void ERROR(int code, String text)
    {
        log(code, LEVEL.ERR, text, null);
    }
    public static void ERROR(int code, String text, Exception e)
    {
        log(code, LEVEL.ERR, text, e);
    }

    private static void log(int code, LEVEL lvl, String text, @Nullable Exception e)
    {
        String time = getDateString();
        String errorCode = code == 0 ? "[---]" : "[" + code + "]";
        String prefix = "[" + lvl + "]";

        String color = WHITE;
        if(lvl == LEVEL.WRN)
            color = YELLOW;
        else if(lvl == LEVEL.ERR)
            color = RED;
        System.out.println(color + time + " " + prefix + " " + errorCode + " " + text + RESET);
        if(e != null)
            e.printStackTrace();

        if(lvl != LEVEL.INF)
        {
            try
            {
                if(!errorFile.exists())
                {
                    createFileIfNotExists(localStorage, false);
                    createFileIfNotExists(logFolder, false);
                    createFileIfNotExists(errorFile, true);
                }
                List<String> fileContent = new ArrayList<>(Files.readAllLines(errorFile.toPath(), StandardCharsets.UTF_8));

                fileContent.add(time + prefix + errorCode + text);
                if(e != null) fileContent.add("\t" + Arrays.toString(e.getStackTrace())
                        .replace(", ", "\n\t")
                        .replace("[", "").replace("]", ""));

                Files.write(errorFile.toPath(), fileContent, StandardCharsets.UTF_8);
            } catch(IOException ex)
            {
                time = "[" + Date.from(Instant.now()) + "]";
                prefix = "[ERR]";
                errorCode = "[996]";
                System.out.println(time + RED + prefix + errorCode + "Something went wrong when writing to the log-file" + RESET);
                ex.printStackTrace();
            }
        }
    }

    private static String getDateString()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        return "[" + dtf.format(now) + "]";
    }

    private static String getLogfileName()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();

        return dtf.format(now);
    }

    private static void createFileIfNotExists(File file, boolean isFile)
    {
        if(!file.exists())
        {
            if(isFile)
            {
                try
                {
                    file.createNewFile();
                } catch(IOException e)
                {
                    Logger.ERROR(997, "Could not create file: " + file, e);
                    System.exit(997);
                }
            } else
                file.mkdir();
        }
    }

    public enum LEVEL
    {
        INF,
        WRN,
        ERR
    }
}
