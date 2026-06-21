package fr.allagnat.casinomod.screen.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

public class CustomSoundButton extends ButtonWidget {
    private final SoundEvent sound;
    private final float pitch;
    private SoundManager soundManager;

    public CustomSoundButton(int x, int y, int width, int height,
                             Text message, PressAction onPress, SoundEvent soundEvent, float pitch) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.sound = soundEvent;
        this.pitch = pitch;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        this.soundManager = soundManager;
        soundManager.play(PositionedSoundInstance.master(sound, pitch));
    }
}
