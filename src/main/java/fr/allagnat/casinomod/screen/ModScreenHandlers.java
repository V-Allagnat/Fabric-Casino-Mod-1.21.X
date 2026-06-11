package fr.allagnat.casinomod.screen;

import fr.allagnat.casinomod.CasinoMod;
import fr.allagnat.casinomod.screen.custom.BlackjackTableScreenHandler;
import fr.allagnat.casinomod.screen.custom.ChipConverterScreenHandler;
import fr.allagnat.casinomod.screen.custom.RouletteScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<ChipConverterScreenHandler> CHIP_CONVERTER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoMod.MOD_ID, "chip_converter_screen_handler"),
                    new ExtendedScreenHandlerType<>(ChipConverterScreenHandler::new, BlockPos.PACKET_CODEC));

    public static final ScreenHandlerType<BlackjackTableScreenHandler> BLACKJACK_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoMod.MOD_ID, "blackjack_table_screen_handler"),
                    new ExtendedScreenHandlerType<>(BlackjackTableScreenHandler::new, BlockPos.PACKET_CODEC));

    public static final ScreenHandlerType<RouletteScreenHandler> ROULETTE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(CasinoMod.MOD_ID, "roulette_screen_handler"),
                    new ExtendedScreenHandlerType<>(RouletteScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void registerScreenHandlers() {
        CasinoMod.LOGGER.info("Registering Screen Handlers for " + CasinoMod.MOD_ID);
    }
}
