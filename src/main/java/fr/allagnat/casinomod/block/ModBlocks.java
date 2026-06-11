package fr.allagnat.casinomod.block;

import fr.allagnat.casinomod.CasinoMod;
import fr.allagnat.casinomod.block.custom.BlackjackTableBlock;
import fr.allagnat.casinomod.block.custom.ChipConverterBlock;
import fr.allagnat.casinomod.block.custom.RouletteBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block CHIP_CONVERTER = registerBlock("chip_converter",
            new ChipConverterBlock(AbstractBlock.Settings.create()
                    .strength(-1.0f, 3600000.0f)
                    .mapColor(MapColor.PALE_PURPLE)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .nonOpaque()));

    public static final Block BLACKJACK_TABLE = registerBlock("blackjack_table",
            new BlackjackTableBlock(AbstractBlock.Settings.create()
                    .strength(-1.0f, 3600000.0f)
                    .mapColor(MapColor.OAK_TAN)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()));

    public static final Block ROULETTE = registerBlock("roulette",
            new RouletteBlock(AbstractBlock.Settings.create()
                    .strength(-1.0f, 3600000.0f)
                    .mapColor(MapColor.BLACK)
                    .sounds(BlockSoundGroup.STONE)
                    .nonOpaque()));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(CasinoMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(CasinoMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        CasinoMod.LOGGER.info("Registering Mod Blocks for " + CasinoMod.MOD_ID);
    }
}
