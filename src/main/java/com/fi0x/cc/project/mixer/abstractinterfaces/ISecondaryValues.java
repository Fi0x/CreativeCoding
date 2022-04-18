package com.fi0x.cc.project.mixer.abstractinterfaces;

import java.util.ArrayList;

public interface ISecondaryValues
{
    ArrayList<String> getSecondaryValueNames();
    void updateSecondaryValue(String valueName, int valueChange);
    String getSecondaryValue(String valueName);
}
