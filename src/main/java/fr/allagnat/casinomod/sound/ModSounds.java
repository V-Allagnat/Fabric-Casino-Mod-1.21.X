package fr.allagnat.casinomod.sound;

import fr.allagnat.casinomod.CasinoMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent ROULETTE_ROLL = registerSoundEvent("roulette_roll");

    private static SoundEvent registerSoundEvent(String name) {
        return Registry.register(Registries.SOUND_EVENT, Identifier.of(CasinoMod.MOD_ID, name),
                SoundEvent.of(Identifier.of(CasinoMod.MOD_ID, name)));
    }

    public static void registerSounds() {
        CasinoMod.LOGGER.info("Registering Mod Sounds for " + CasinoMod.MOD_ID);
    }
}
