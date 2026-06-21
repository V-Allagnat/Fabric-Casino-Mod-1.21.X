package fr.allagnat.casinomod.item;

import fr.allagnat.casinomod.CasinoMod;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItems {
    public static Map<Item, Integer> valueMap = new HashMap<>();

    public static final Item CHIP_1 = registerItem("chip_1", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_1.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    public static final Item CHIP_5 = registerItem("chip_5", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_5.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    public static final Item CHIP_10 = registerItem("chip_10", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_10.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    public static final Item CHIP_25 = registerItem("chip_25", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_25.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    public static final Item CHIP_50 = registerItem("chip_50", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_50.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    public static final Item CHIP_100 = registerItem("chip_100", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_100.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    public static final Item CHIP_500 = registerItem("chip_500", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_500.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    public static final Item CHIP_1000 = registerItem("chip_1000", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_1000.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    public static final Item PIGRON = registerItem("pigron", new Item(new Item.Settings().food(ModFoodComponents.PIGRON)) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.literal("§8§oHop Pigroned§r"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    // INITIALIZE valueMap RIGHT AFTER REGISTERING ITEMS TO AVOID NULL ISSUES
    static {
        valueMap.put(ModItems.CHIP_1, 1);
        valueMap.put(ModItems.CHIP_5, 5);
        valueMap.put(ModItems.CHIP_10, 10);
        valueMap.put(ModItems.CHIP_25, 25);
        valueMap.put(ModItems.CHIP_50, 50);
        valueMap.put(ModItems.CHIP_100, 100);
        valueMap.put(ModItems.CHIP_500, 500);
        valueMap.put(ModItems.CHIP_1000, 1000);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(CasinoMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        CasinoMod.LOGGER.info("Registering Mod Items for " + CasinoMod.MOD_ID);
    }
}
