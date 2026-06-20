package fr.allagnat.casinomod.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.allagnat.casinomod.CasinoMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RouletteScreen extends HandledScreen<RouletteScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of(CasinoMod.MOD_ID, "textures/gui/roulette/roulette_gui.png");

    public RouletteScreen(RouletteScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        // get the 'Inventory' label way out of the screen
        playerInventoryTitleX += 999999;
        playerInventoryTitleY += 999999;

        addDrawableChild(new ButtonWidget.Builder(Text.translatable("display.casinomod.roulette.button_0"), button -> {

        })
                .build());
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
