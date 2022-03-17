package com.fi0x.cc.project.synth.synthesizers;

public class MusicConverter
{
    public static int getNoteValue(char note, int octave)
    {
        int correctNote = octave * 12;
        switch(note)
        {
            case 'C':
                break;
            case 'D':
                correctNote += 2;
                break;
            case 'E':
                correctNote += 4;
                break;
            case 'F':
                correctNote += 5;
                break;
            case 'G':
                correctNote += 7;
                break;
            case 'A':
                correctNote += 9;
                break;
            case 'B':
                correctNote += 11;
                break;
            case 'c':
                correctNote += 1;
                break;
            case 'd':
                correctNote += 3;
                break;
            case 'f':
                correctNote += 6;
                break;
            case 'g':
                correctNote += 8;
                break;
            case 'a':
                correctNote += 10;
                break;
            default:
                correctNote = 0;
                break;
        }
        return Math.min(correctNote, 127);
    }
}
