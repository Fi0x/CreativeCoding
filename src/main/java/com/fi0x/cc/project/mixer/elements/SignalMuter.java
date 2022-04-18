package com.fi0x.cc.project.mixer.elements;

import com.fi0x.cc.project.gui.mixer.MainMixerWindow;
import com.fi0x.cc.project.mixer.MixerSignal;
import com.fi0x.cc.project.mixer.abstractinterfaces.AbstractElement;
import com.fi0x.cc.project.mixer.abstractinterfaces.INumberProvider;
import com.fi0x.cc.project.mixer.abstractinterfaces.ISignalModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignalMuter extends AbstractElement implements ISignalModifier
{
    private boolean allowPass = true;

    private final Map<UUID, Boolean> handledSignals = new HashMap<>();

    private final ArrayList<INumberProvider> connectedNumberProviders = new ArrayList<>();

    public SignalMuter(MainMixerWindow parentScreen, int x, int y)
    {
        super(parentScreen, x, y);

        startColorAnimation();
    }

    @Override
    public void receiveMidi(MixerSignal msg)
    {
        if(nextLink == null)
            return;

        if(connectedNumberProviders.size() > 0)
        {
            int num = 0;
            for(INumberProvider np : connectedNumberProviders)
                num += np.getCurrentNumber();
            allowPass = num > 0;
        }

        handledSignals.put(msg.id, allowPass);
        if(handledSignals.get(msg.id))
        {
            nextLink.receiveMidi(msg);
            if(!msg.hasMore)
                handledSignals.remove(msg.id);
        }
    }
    @Override
    public void changeMainValue(int valueChange)
    {
        allowPass = valueChange > 0;
    }
    @Override
    public String getMainValueName()
    {
        return "Passable";
    }
    @Override
    public String getMainValue()
    {
        return String.valueOf(allowPass);
    }
    @Override
    public void addNumberProvider(INumberProvider provider)
    {
        connectedNumberProviders.add(provider);
    }
    @Override
    public void removeNumberProvider(INumberProvider provider)
    {
        connectedNumberProviders.remove(provider);
    }
    @Override
    public String getDisplayString()
    {
        return "Signal Muter\n" + (allowPass ? "Open" : "Muted");
    }
}
