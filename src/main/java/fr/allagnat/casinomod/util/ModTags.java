package fr.allagnat.casinomod.util;

import fr.allagnat.casinomod.CasinoMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(CasinoMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CHIP_ITEMS = createTag("chip_items");

        public static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(CasinoMod.MOD_ID, name));
        }
    }
}
