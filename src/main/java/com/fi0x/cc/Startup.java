package com.fi0x.cc;

import com.fi0x.cc.week1.Exercise1;
import processing.core.PApplet;

import java.util.Scanner;

public class Startup
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to my Creative Coding Lecture Project!");
        System.out.print("Please type in the number of the exercise you would like to see(1-8): ");
        String input = sc.next();

        switch(input)
        {
            case "1":
                System.out.println("Starting exercise 1-1");
                PApplet.main("com.fi0x.cc.week1.Exercise1");
                break;
            case "2":
                System.out.println("Starting exercise 1-3");
                PApplet.main("com.fi0x.cc.week1.Exercise3");
                break;
            case "3":
                System.out.println("Starting exercise 1-3 variant");
                PApplet.main("com.fi0x.cc.week1.Exercise3Different");
                break;
            case "4":
                System.out.println("Starting exercise 1-4");
                PApplet.main("com.fi0x.cc.week1.Exercise4");
                break;
            case "5":
                System.out.println("Starting exercise 1-4 variant");
                PApplet.main("com.fi0x.cc.week1.Exercise4Different");
                break;
            case "6":
                System.out.println("Starting exercise 1-4 variant");
                PApplet.main("com.fi0x.cc.week1.Exercise4Pointillism");
                break;
            case "7":
                break;
            case "8":
                break;
            default:
                System.out.println("No valid input, ending the program...");
                break;
        }
    }
}
