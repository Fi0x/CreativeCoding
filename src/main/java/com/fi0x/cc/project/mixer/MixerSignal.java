package com.fi0x.cc.project.mixer;

import javax.sound.midi.ShortMessage;
import java.util.UUID;

public class MixerSignal
{
    public ShortMessage midiMessage = null;
    public boolean hasMore = false;
    public UUID id = UUID.randomUUID();
}
