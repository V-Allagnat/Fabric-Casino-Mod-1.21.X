package fr.allagnat.casinomod.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;


public class TransparentButton extends ButtonWidget {
    public TransparentButton(int x, int y, int width, int height, PressAction onPress) {
        super(x, y, width, height, Text.literal(""), onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // empty since there is no text / texture to draw
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ENTITY_ITEM_PICKUP, 1.0f));
    }
}
