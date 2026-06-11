package fr.allagnat.casinomod.item;

import fr.allagnat.casinomod.CasinoMod;
import fr.allagnat.casinomod.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup CASINO_MOD_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(CasinoMod.MOD_ID, "casino_mod"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.CHIP_100))
                    .displayName(Text.translatable("itemgroup.casinomod.casino_mod"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.CHIP_CONVERTER);
                        entries.add(ModBlocks.BLACKJACK_TABLE);
                        entries.add(ModBlocks.ROULETTE);
                        entries.add(ModItems.CHIP_1);
                        entries.add(ModItems.CHIP_5);
                        entries.add(ModItems.CHIP_10);
                        entries.add(ModItems.CHIP_25);
                        entries.add(ModItems.CHIP_50);
                        entries.add(ModItems.CHIP_100);
                        entries.add(ModItems.CHIP_500);
                        entries.add(ModItems.CHIP_1000);
                        entries.add(ModItems.PIGRON);
                    }).build());

    public static void registerItemGroups() {
        CasinoMod.LOGGER.info("Registering Item Groups for " + CasinoMod.MOD_ID);
    }
}
