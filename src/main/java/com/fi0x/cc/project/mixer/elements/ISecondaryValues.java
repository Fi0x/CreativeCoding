package com.fi0x.cc.project.mixer.elements;

import java.util.ArrayList;

public interface ISecondaryValues
{
    //TODO: Use to change more values
    ArrayList<String> getSecondaryValueNames();
    void updateSecondaryValue(String valueName, int valueChange);
}
