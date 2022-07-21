package com.fi0x.cc.project.midi;

import com.fi0x.cc.Main;
import io.fi0x.javalogger.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class MidiSignalHelper
{
    public static String getCombinedData(int msb, int lsb)
    {
        return expandSmallByte(msb, 7) + expandSmallByte(lsb, 7);
    }
    public static String expandSmallByte(int originalByte, int desiredLength)
    {
        StringBuilder originalByteRepresentation = new StringBuilder(Integer.toBinaryString(originalByte));
        while(originalByteRepresentation.length() < desiredLength)
            originalByteRepresentation.insert(0, "0");

        return originalByteRepresentation.toString();
    }

    public static void changeChannel(ShortMessage originalMsg, int newChannel, boolean isIncrement)
    {
        MidiSignalInfo info = MidiSignalInfo.getSignalType(originalMsg.getStatus());
        assert info != null;

        info.channel = isIncrement ? originalMsg.getChannel() + newChannel : newChannel;
        try
        {
            originalMsg.setMessage(info.getStatusByte(), originalMsg.getData1(), originalMsg.getData2());
        } catch(InvalidMidiDataException ignored)
        {
            Logger.log("Could not change the channel of a midi message", String.valueOf(Main.Template.DEBUG_WARNING));
        }
    }
    public static void changeNote(ShortMessage originalMsg, int newNote, boolean isIncrement)
    {
        int updatedNote = isIncrement ? originalMsg.getData1() + newNote : newNote;
        try
        {
            originalMsg.setMessage(originalMsg.getStatus(), updatedNote, originalMsg.getData2());
        } catch(InvalidMidiDataException ignored)
        {
            Logger.log("Could not change the note of a midi message", String.valueOf(Main.Template.DEBUG_WARNING));
        }
    }
    public static void changeVolume(ShortMessage originalMsg, int newVolume, boolean isIncrement)
    {
        int updatedVolume = isIncrement ? originalMsg.getData2() + newVolume : newVolume;
        try
        {
            originalMsg.setMessage(originalMsg.getStatus(), originalMsg.getData1(), updatedVolume);
        } catch(InvalidMidiDataException ignored)
        {
            Logger.log("Could not change the volume of a midi message", String.valueOf(Main.Template.DEBUG_WARNING));
        }
    }
    public static void changeAllData(ShortMessage originalMsg, int newData, boolean isIncrement)
    {
        String expanded = expandSmallByte(newData, 14);
        int firstData = Integer.parseInt(expanded.substring(0, 4), 2);
        int secondData = Integer.parseInt(expanded.substring(4), 2);

        if(isIncrement)
        {
            firstData += originalMsg.getData1();
            secondData += originalMsg.getData2();
        }

        try
        {
            originalMsg.setMessage(originalMsg.getStatus(), firstData, secondData);
        } catch(InvalidMidiDataException ignored)
        {
            Logger.log("Could not change both data fields of a midi message", String.valueOf(Main.Template.DEBUG_WARNING));
        }
    }
}
