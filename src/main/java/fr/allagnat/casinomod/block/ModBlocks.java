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
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModBlocks {
    public static final Block CHIP_CONVERTER = registerBlock("chip_converter",
            new ChipConverterBlock(AbstractBlock.Settings.create()
                    .strength(-1.0f, 3600000.0f)
                    .mapColor(MapColor.PURPLE)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .nonOpaque())
    );

    public static final Block BLACKJACK_TABLE = registerBlock("blackjack_table",
            new BlackjackTableBlock(AbstractBlock.Settings.create()
                    .strength(-1.0f, 3600000.0f)
                    .mapColor(MapColor.BRIGHT_RED)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()),
            "§8§otadamoli, jvais te DÉMOLIR§r"
    );

    public static final Block ROULETTE = registerBlock("roulette",
            new RouletteBlock(AbstractBlock.Settings.create()
                    .strength(-1.0f, 3600000.0f)
                    .mapColor(MapColor.BRIGHT_RED)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque())
    );

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(CasinoMod.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block, String tooltipText) {
        registerBlockItem(name, block, tooltipText);
        return Registry.register(Registries.BLOCK, Identifier.of(CasinoMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(CasinoMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    private static void registerBlockItem(String name, Block block, String tooltipText) {
        Registry.register(Registries.ITEM, Identifier.of(CasinoMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()) {
                    @Override
                    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                        tooltip.add(Text.literal(tooltipText));
                        super.appendTooltip(stack, context, tooltip, type);
                    }
                }
            );
    }

    public static void registerModBlocks() {
        CasinoMod.LOGGER.info("Registering Mod Blocks for " + CasinoMod.MOD_ID);
    }
}
