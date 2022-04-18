package com.fi0x.cc.project.mixer.abstractinterfaces;

public interface ISignalModifier
{
    void addNumberProvider(INumberProvider provider);
    void removeNumberProvider(INumberProvider provider);
}
