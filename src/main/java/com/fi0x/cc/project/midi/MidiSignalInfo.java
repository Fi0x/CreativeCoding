package com.fi0x.cc.project.midi;

import java.util.HashMap;
import java.util.Map;

public class MidiSignalInfo
{
    public final MidiSignalName NAME;
    public int channel;

    private static final Map<MidiSignalName, String> bitMapping = new HashMap<>()
    {{
        put(MidiSignalName.NoteOn, "1001");
        put(MidiSignalName.NoteOff, "1000");
        put(MidiSignalName.PolyPressure, "1010");
        put(MidiSignalName.ControlChange, "1011");
        put(MidiSignalName.ProgramChange, "1100");
        put(MidiSignalName.ChannelPressure, "1101");
        put(MidiSignalName.PitchBend, "1110");
    }};

    public MidiSignalInfo(MidiSignalName type, int channel)
    {
        NAME = type;
        this.channel = channel;
    }

    public static MidiSignalInfo getSignalType(int statusByte)
    {
        String status = Integer.toBinaryString(statusByte).substring(0, 4);
        int midiChannel = Integer.parseInt(Integer.toBinaryString(statusByte).substring(4), 2);

        for(Map.Entry<MidiSignalName, String> entry : bitMapping.entrySet())
        {
            if(entry.getValue().equals(status))
                return new MidiSignalInfo(entry.getKey(), midiChannel);
        }

        return null;
    }

    public int getStatusByte()
    {
        return Integer.parseInt(bitMapping.get(NAME) + MidiSignalHelper.expandSmallByte(this.channel, 4), 2);
    }

    public enum MidiSignalName
    {
        NoteOn,
        NoteOff,
        PolyPressure,
        ControlChange,
        ProgramChange,
        ChannelPressure,
        PitchBend
    }
}
