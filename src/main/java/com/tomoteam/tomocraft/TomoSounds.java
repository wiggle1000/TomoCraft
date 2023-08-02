package com.tomoteam.tomocraft;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class TomoSounds
{
    public static final SoundEvent DEER_SCARE = SoundEvent.of(TomoCraft.ModId("deer_scare"));
    public static final SoundEvent FOUNTAIN = SoundEvent.of(TomoCraft.ModId("fountain"));
    public static void RegisterSounds()
    {
        AddSound(DEER_SCARE);
        AddSound(FOUNTAIN);
    }

    private static void AddSound(SoundEvent soundEvent)
    {
        Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent);
    }
}
