package com.fi0x.cc.project.mixer.elements;

import java.util.ArrayList;

public interface ISecondaryValues
{
    ArrayList<String> getSecondaryValueNames();
    void updateSecondaryValue(String valueName, int valueChange);
    String getSecondaryValue(String valueName);
}
