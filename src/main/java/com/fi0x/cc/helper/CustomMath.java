package com.fi0x.cc.helper;

public class CustomMath
{
    public static float clamp(float value, float min, float max)
    {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max)
    {
        return Math.max(min, Math.min(max, value));
    }
}
