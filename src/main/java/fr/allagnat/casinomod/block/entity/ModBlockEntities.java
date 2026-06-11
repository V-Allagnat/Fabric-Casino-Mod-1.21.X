package fr.allagnat.casinomod.block.entity;

import fr.allagnat.casinomod.CasinoMod;
import fr.allagnat.casinomod.block.ModBlocks;
import fr.allagnat.casinomod.block.entity.custom.BlackjackTableBlockEntity;
import fr.allagnat.casinomod.block.entity.custom.ChipConverterBlockEntity;
import fr.allagnat.casinomod.block.entity.custom.RouletteBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<BlackjackTableBlockEntity> BLACKJACK_TABLE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(CasinoMod.MOD_ID, "blackjack_table_be"),
                    BlockEntityType.Builder.create(BlackjackTableBlockEntity::new, ModBlocks.BLACKJACK_TABLE).build(null));

    public static final BlockEntityType<ChipConverterBlockEntity> CHIP_CONVERTER_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(CasinoMod.MOD_ID, "chip_converter_be"),
                    BlockEntityType.Builder.create(ChipConverterBlockEntity::new, ModBlocks.CHIP_CONVERTER).build(null));

    public static final BlockEntityType<RouletteBlockEntity> ROULETTE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(CasinoMod.MOD_ID, "roulette_be"),
                    BlockEntityType.Builder.create(RouletteBlockEntity::new, ModBlocks.ROULETTE).build(null));

    public static void registerBlockEntities() {
        CasinoMod.LOGGER.info("Registering Block Entities for " + CasinoMod.MOD_ID);
    }

}
