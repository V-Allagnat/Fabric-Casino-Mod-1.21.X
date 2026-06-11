package fr.allagnat.casinomod.item;

import fr.allagnat.casinomod.CasinoMod;
import fr.allagnat.casinomod.block.entity.custom.BlackjackTableBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItems {
    public static Map<Item, Integer> valueMap = new HashMap<>();

    private static ActionResult useOnBlockOverride(ItemUsageContext context, Item chip) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (player == null || !(blockEntity instanceof BlackjackTableBlockEntity blackjackTable))
            return ActionResult.FAIL;
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        if ((blackjackTable.getStack(0).getItem() == chip
                || blackjackTable.isEmpty())
                && player.isSneaking())
            /* && !world.isClient()) */{
            ItemStack tableStack = ((BlackjackTableBlockEntity) blockEntity).getStack(0);
            if (blackjackTable.isEmpty()) {
                blackjackTable.setStack(0, stack.copyWithCount(stack.getCount()));
                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            } else if (tableStack.getCount() + stack.getCount() <= 64) {
                tableStack.increment(stack.getCount());
                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            } else {
                int stackIncrement = 64 - tableStack.getCount();
                tableStack.increment(stackIncrement);
                int currentStackCount = player.getStackInHand(Hand.MAIN_HAND).getCount();
                ItemStack newStack = chip.getDefaultStack();
                newStack.setCount(currentStackCount - stackIncrement);
                player.setStackInHand(Hand.MAIN_HAND, newStack);
            }
        }
        world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 2f, 2f);
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 0);
        return world.getBlockEntity(pos) instanceof BlackjackTableBlockEntity ? ActionResult.SUCCESS : ActionResult.FAIL;
    }

    public static final Item CHIP_1 = registerItem("chip_1", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_1.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            return ModItems.useOnBlockOverride(context, ModItems.CHIP_1);
        }
    });

    public static final Item CHIP_5 = registerItem("chip_5", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_5.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            return ModItems.useOnBlockOverride(context, ModItems.CHIP_5);
        }
    });

    public static final Item CHIP_10 = registerItem("chip_10", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_10.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            return ModItems.useOnBlockOverride(context, ModItems.CHIP_10);
        }
    });

    public static final Item CHIP_25 = registerItem("chip_25", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_25.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            return ModItems.useOnBlockOverride(context, ModItems.CHIP_25);
        }
    });

    public static final Item CHIP_50 = registerItem("chip_50", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_50.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            return ModItems.useOnBlockOverride(context, ModItems.CHIP_50);
        }
    });

    public static final Item CHIP_100 = registerItem("chip_100", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_100.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            return ModItems.useOnBlockOverride(context, ModItems.CHIP_100);
        }
    });

    public static final Item CHIP_500 = registerItem("chip_500", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_500.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            return ModItems.useOnBlockOverride(context, ModItems.CHIP_500);
        }
    });

    public static final Item CHIP_1000 = registerItem("chip_1000", new Item(new Item.Settings()) {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.casinomod.chip_1000.tooltip"));
            super.appendTooltip(stack, context, tooltip, type);
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            return ModItems.useOnBlockOverride(context, ModItems.CHIP_1000);
        }
    });

    public static final Item PIGRON = registerItem("pigron", new Item(new Item.Settings()) {
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
