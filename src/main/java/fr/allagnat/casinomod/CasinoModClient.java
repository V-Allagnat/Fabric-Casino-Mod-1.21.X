package fr.allagnat.casinomod;

import fr.allagnat.casinomod.block.entity.ModBlockEntities;
import fr.allagnat.casinomod.block.entity.renderer.BlackjackTableBlockEntityRenderer;
import fr.allagnat.casinomod.block.entity.renderer.ChipConverterBlockEntityRenderer;
import fr.allagnat.casinomod.block.entity.renderer.RouletteBlockEntityRenderer;
import fr.allagnat.casinomod.screen.ModScreenHandlers;
import fr.allagnat.casinomod.screen.custom.BlackjackTableScreen;
import fr.allagnat.casinomod.screen.custom.ChipConverterScreen;
import fr.allagnat.casinomod.screen.custom.RouletteScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class CasinoModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.CHIP_CONVERTER_BE, ChipConverterBlockEntityRenderer::new);
        HandledScreens.register(ModScreenHandlers.CHIP_CONVERTER_SCREEN_HANDLER, ChipConverterScreen::new);
        BlockEntityRendererFactories.register(ModBlockEntities.BLACKJACK_TABLE_BE, BlackjackTableBlockEntityRenderer::new);
        HandledScreens.register(ModScreenHandlers.BLACKJACK_TABLE_SCREEN_HANDLER, BlackjackTableScreen::new);
        BlockEntityRendererFactories.register(ModBlockEntities.ROULETTE_BE, RouletteBlockEntityRenderer::new);
        HandledScreens.register(ModScreenHandlers.ROULETTE_SCREEN_HANDLER, RouletteScreen::new);
    }
}
