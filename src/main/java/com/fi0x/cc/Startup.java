package com.fi0x.cc;

import com.fi0x.cc.logging.Logger;
import com.fi0x.cc.project.synth.SynthPlayer;
import processing.core.PApplet;

import java.util.Scanner;

public class Startup
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        Logger.INFO("Welcome to my Creative Coding Lecture Project!");
        showMainMenu(sc);
    }

    private static void showMainMenu(Scanner sc)
    {
        Logger.INFO("Please type in the number of the exercise you would like to see(1-8): ");
        String input = sc.next();

        switch(input)
        {
            case "1":
                Logger.INFO("Starting exercise 1-1");
                PApplet.main("com.fi0x.cc.week1.Exercise1");
                break;
            case "2":
                Logger.INFO("Starting exercise 1-3");
                PApplet.main("com.fi0x.cc.week1.Exercise3");
                break;
            case "3":
                Logger.INFO("Starting exercise 1-3 variant");
                PApplet.main("com.fi0x.cc.week1.Exercise3Different");
                break;
            case "4":
                Logger.INFO("Starting exercise 1-4");
                PApplet.main("com.fi0x.cc.week1.Exercise4");
                break;
            case "5":
                Logger.INFO("Starting exercise 1-4 variant");
                PApplet.main("com.fi0x.cc.week1.Exercise4Different");
                break;
            case "6":
                Logger.INFO("Starting exercise 1-4 variant");
                PApplet.main("com.fi0x.cc.week1.Exercise4Pointillism");
                break;
            case "7":
                Logger.INFO("Starting Synthesizer");
                SynthPlayer.getInstance().startListening();
                break;
            case "8":
                break;
            default:
                Logger.INFO("No valid input, please try a valid number(1-8): ");
                showMainMenu(sc);
                break;
        }
    }
}
