package fr.allagnat.casinomod.screen.widget;

import fr.allagnat.casinomod.CasinoMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TransparentButton extends ButtonWidget {

    private final Identifier texture = Identifier.of(CasinoMod.MOD_ID, "textures/button/transparent.png");

    public TransparentButton(int x, int y, int width, int height, PressAction onPress, Text text) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta); // FIXME make it transparent
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ENTITY_ITEM_PICKUP, 2.0f));
    }
}
