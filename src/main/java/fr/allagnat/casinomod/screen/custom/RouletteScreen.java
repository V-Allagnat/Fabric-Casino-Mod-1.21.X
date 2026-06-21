package fr.allagnat.casinomod.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.allagnat.casinomod.CasinoMod;
import fr.allagnat.casinomod.item.ModItems;
import fr.allagnat.casinomod.screen.widget.CustomSoundButton;
import fr.allagnat.casinomod.screen.widget.TransparentButton;
import fr.allagnat.casinomod.sound.ModSounds;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Random;

public class RouletteScreen extends HandledScreen<RouletteScreenHandler> {
    private static final Identifier GUI_TEXTURE =
            Identifier.of(CasinoMod.MOD_ID, "textures/gui/roulette/roulette_gui.png");

    private ButtonWidget.PressAction onBetPress(String name) {
        return button -> {
            if (handler.totalBet >= handler.slots.getFirst().getStack().getCount()) {
                return;
            }
            handler.totalBet++;
            if (handler.betMap.get(name) == null) {
                handler.betMap.put(name, new ItemStack(handler.slots.getFirst().getStack().getItem(), 1));
            } else {
                handler.betMap.get(name).increment(1);
            }
        };
    }

    private final ButtonWidget.PressAction roll = button -> {
        // validity check
        if (handler.totalBet == 0 || handler.totalBet > handler.slots.getFirst().getStack().getCount()) {
            handler.getPlayer().sendMessage(Text.translatable("message.casinomod.roulette.invalid_bet"));
            handler.betMap = new HashMap<>();
            handler.totalBet = 0;
            return;
        }
        for (ItemStack s : handler.betMap.values()) {
            if (s.getItem() != handler.slots.getFirst().getStack().getItem()) {
                handler.getPlayer().sendMessage(Text.translatable("message.casinomod.roulette.invalid_bet"));
                handler.betMap = new HashMap<>();
                handler.totalBet = 0;
            }
        }

        // remove chips from the inventory slot
        handler.slots.getFirst().getStack().decrement(handler.totalBet);
        ClientPlayNetworking.send(new StackDecrementPayload(handler.getEntityPos(), handler.totalBet));

        // roll
        int roll = new Random().nextInt(0, 37);
        handler.lastRoll = RouletteScreenHandler.LOOKUP.get(roll);

        handler.getPlayer().getWorld().getBlockEntity(handler.getEntityPos()).markDirty();
        handler.getPlayer().getWorld().updateListeners(
                handler.getEntityPos(),
                handler.getPlayer().getWorld().getBlockEntity(handler.getEntityPos()).getCachedState(),
                handler.getPlayer().getWorld().getBlockEntity(handler.getEntityPos()).getCachedState(),
                Block.NOTIFY_ALL
        );

        // reward accordingly

        // zero
        if (roll == 0) {
            handler.giveReward("0", 36);
            handler.betMap = new HashMap<>();
            handler.totalBet = 0;
            return;
        }

        // thirds
        if (roll <= 12) {
            handler.giveReward("1-12", 3);
        } else if (roll <= 24) {
            handler.giveReward("13-24", 3);
        } else {
            handler.giveReward("25-36", 3);
        }

        // halves
        if (roll <= 18) {
            handler.giveReward("1-18", 2);
        } else {
            handler.giveReward("19-36", 2);
        }

        // colors
        if (handler.isRed(roll)) {
            handler.giveReward("red", 2);
        } else {
            handler.giveReward("black", 2);
        }

        // single number
        handler.giveReward(String.valueOf(roll), 36);

        handler.betMap = new HashMap<>();
        handler.totalBet = 0;
    };

    private final ButtonWidget btnRoll = new CustomSoundButton(0, 0, 40, 16,
            Text.translatable("display.casinomod.roulette.button_roll"),
            roll,
            ModSounds.ROULETTE_ROLL,
            0.8f
    );

    private final TransparentButton btnRed = new TransparentButton(0, 0, 12, 19, onBetPress("red"));
    private final TransparentButton btnBlack = new TransparentButton(0, 0, 12, 19, onBetPress("black"));
    private final TransparentButton btn1_18 = new TransparentButton(0, 0, 28, 12, onBetPress("1-18"));
    private final TransparentButton btn19_36 = new TransparentButton(0, 0, 28, 12, onBetPress("19-36"));
    private final TransparentButton btn1_12 = new TransparentButton(0, 0, 28, 12, onBetPress("1-12"));
    private final TransparentButton btn13_24 = new TransparentButton(0, 0, 28, 12, onBetPress("13-24"));
    private final TransparentButton btn25_36 = new TransparentButton(0, 0, 28, 12, onBetPress("25-36"));
    private final TransparentButton btn0 = new TransparentButton(0, 0, 12, 12, onBetPress("0"));
    private final TransparentButton btn1 = new TransparentButton(0, 0, 12, 12, onBetPress("1"));
    private final TransparentButton btn2 = new TransparentButton(0, 0, 12, 12, onBetPress("2"));
    private final TransparentButton btn3 = new TransparentButton(0, 0, 12, 12, onBetPress("3"));
    private final TransparentButton btn4 = new TransparentButton(0, 0, 12, 12, onBetPress("4"));
    private final TransparentButton btn5 = new TransparentButton(0, 0, 12, 12, onBetPress("5"));
    private final TransparentButton btn6 = new TransparentButton(0, 0, 12, 12, onBetPress("6"));
    private final TransparentButton btn7 = new TransparentButton(0, 0, 12, 12, onBetPress("7"));
    private final TransparentButton btn8 = new TransparentButton(0, 0, 12, 12, onBetPress("8"));
    private final TransparentButton btn9 = new TransparentButton(0, 0, 12, 12, onBetPress("9"));
    private final TransparentButton btn10 = new TransparentButton(0, 0, 12, 12, onBetPress("10"));
    private final TransparentButton btn11 = new TransparentButton(0, 0, 12, 12, onBetPress("11"));
    private final TransparentButton btn12 = new TransparentButton(0, 0, 12, 12, onBetPress("12"));
    private final TransparentButton btn13 = new TransparentButton(0, 0, 12, 12, onBetPress("13"));
    private final TransparentButton btn14 = new TransparentButton(0, 0, 12, 12, onBetPress("14"));
    private final TransparentButton btn15 = new TransparentButton(0, 0, 12, 12, onBetPress("15"));
    private final TransparentButton btn16 = new TransparentButton(0, 0, 12, 12, onBetPress("16"));
    private final TransparentButton btn17 = new TransparentButton(0, 0, 12, 12, onBetPress("17"));
    private final TransparentButton btn18 = new TransparentButton(0, 0, 12, 12, onBetPress("18"));
    private final TransparentButton btn19 = new TransparentButton(0, 0, 12, 12, onBetPress("19"));
    private final TransparentButton btn20 = new TransparentButton(0, 0, 12, 12, onBetPress("20"));
    private final TransparentButton btn21 = new TransparentButton(0, 0, 12, 12, onBetPress("21"));
    private final TransparentButton btn22 = new TransparentButton(0, 0, 12, 12, onBetPress("22"));
    private final TransparentButton btn23 = new TransparentButton(0, 0, 12, 12, onBetPress("23"));
    private final TransparentButton btn24 = new TransparentButton(0, 0, 12, 12, onBetPress("24"));
    private final TransparentButton btn25 = new TransparentButton(0, 0, 12, 12, onBetPress("25"));
    private final TransparentButton btn26 = new TransparentButton(0, 0, 12, 12, onBetPress("26"));
    private final TransparentButton btn27 = new TransparentButton(0, 0, 12, 12, onBetPress("27"));
    private final TransparentButton btn28 = new TransparentButton(0, 0, 12, 12, onBetPress("28"));
    private final TransparentButton btn29 = new TransparentButton(0, 0, 12, 12, onBetPress("29"));
    private final TransparentButton btn30 = new TransparentButton(0, 0, 12, 12, onBetPress("30"));
    private final TransparentButton btn31 = new TransparentButton(0, 0, 12, 12, onBetPress("31"));
    private final TransparentButton btn32 = new TransparentButton(0, 0, 12, 12, onBetPress("32"));
    private final TransparentButton btn33 = new TransparentButton(0, 0, 12, 12, onBetPress("33"));
    private final TransparentButton btn34 = new TransparentButton(0, 0, 12, 12, onBetPress("34"));
    private final TransparentButton btn35 = new TransparentButton(0, 0, 12, 12, onBetPress("35"));
    private final TransparentButton btn36 = new TransparentButton(0, 0, 12, 12, onBetPress("36"));

    // set button tooltips
    {
        btnRoll.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_roll_tooltip")));
        btnRed.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_red_tooltip")));
        btnBlack.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_black_tooltip")));
        btn1_18.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_1-18_tooltip")));
        btn19_36.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_19-36_tooltip")));
        btn1_12.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_1-12_tooltip")));
        btn13_24.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_13-24_tooltip")));
        btn25_36.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_25-36_tooltip")));
        btn0.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_0_tooltip")));
        btn1.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_1_tooltip")));
        btn2.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_2_tooltip")));
        btn3.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_3_tooltip")));
        btn4.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_4_tooltip")));
        btn5.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_5_tooltip")));
        btn6.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_6_tooltip")));
        btn7.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_7_tooltip")));
        btn8.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_8_tooltip")));
        btn9.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_9_tooltip")));
        btn10.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_10_tooltip")));
        btn11.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_11_tooltip")));
        btn12.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_12_tooltip")));
        btn13.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_13_tooltip")));
        btn14.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_14_tooltip")));
        btn15.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_15_tooltip")));
        btn16.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_16_tooltip")));
        btn17.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_17_tooltip")));
        btn18.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_18_tooltip")));
        btn19.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_19_tooltip")));
        btn20.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_20_tooltip")));
        btn21.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_21_tooltip")));
        btn22.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_22_tooltip")));
        btn23.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_23_tooltip")));
        btn24.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_24_tooltip")));
        btn25.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_25_tooltip")));
        btn26.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_26_tooltip")));
        btn27.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_27_tooltip")));
        btn28.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_28_tooltip")));
        btn29.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_29_tooltip")));
        btn30.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_30_tooltip")));
        btn31.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_31_tooltip")));
        btn32.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_32_tooltip")));
        btn33.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_33_tooltip")));
        btn34.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_34_tooltip")));
        btn35.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_35_tooltip")));
        btn36.setTooltip(Tooltip.of(Text.translatable("display.casinomod.roulette.button_36_tooltip")));
    }

    public RouletteScreen(RouletteScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        // get the 'Inventory' label way out of the screen
        playerInventoryTitleX += 999999;
        playerInventoryTitleY += 999999;

        btnRoll.setPosition(x + 129, y + 9);

        btnRed.setPosition(x + 8, y + 81);
        btnBlack.setPosition(x + 8, y + 105);

        btn1_18.setPosition(x + 8, y + 65);
        btn19_36.setPosition(x + 41, y + 65);
        btn1_12.setPosition(x + 74, y + 65);
        btn13_24.setPosition(x + 107, y + 65);
        btn25_36.setPosition(x + 140, y + 65);

        btn0.setPosition(x + 8, y + 127);

        btn1.setPosition(x + 24, y + 81);
        btn2.setPosition(x + 41, y + 81);
        btn3.setPosition(x + 57, y + 81);
        btn4.setPosition(x + 74, y + 81);
        btn5.setPosition(x + 90, y + 81);
        btn6.setPosition(x + 107, y + 81);
        btn7.setPosition(x + 123, y + 81);
        btn8.setPosition(x + 140, y + 81);
        btn9.setPosition(x + 156, y + 81);

        btn10.setPosition(x + 24, y + 97);
        btn11.setPosition(x + 41, y + 97);
        btn12.setPosition(x + 57, y + 97);
        btn13.setPosition(x + 74, y + 97);
        btn14.setPosition(x + 90, y + 97);
        btn15.setPosition(x + 107, y + 97);
        btn16.setPosition(x + 123, y + 97);
        btn17.setPosition(x + 140, y + 97);
        btn18.setPosition(x + 156, y + 97);

        btn19.setPosition(x + 24, y + 112);
        btn20.setPosition(x + 41, y + 112);
        btn21.setPosition(x + 57, y + 112);
        btn22.setPosition(x + 74, y + 112);
        btn23.setPosition(x + 90, y + 112);
        btn24.setPosition(x + 107, y + 112);
        btn25.setPosition(x + 123, y + 112);
        btn26.setPosition(x + 140, y + 112);
        btn27.setPosition(x + 156, y + 112);

        btn28.setPosition(x + 24, y + 127);
        btn29.setPosition(x + 41, y + 127);
        btn30.setPosition(x + 57, y + 127);
        btn31.setPosition(x + 74, y + 127);
        btn32.setPosition(x + 90, y + 127);
        btn33.setPosition(x + 107, y + 127);
        btn34.setPosition(x + 123, y + 127);
        btn35.setPosition(x + 140, y + 127);
        btn36.setPosition(x + 156, y + 127);

        addDrawableChild(btnRoll);
        addDrawableChild(btnRed);
        addDrawableChild(btnBlack);
        addDrawableChild(btn1_18);
        addDrawableChild(btn19_36);
        addDrawableChild(btn1_12);
        addDrawableChild(btn13_24);
        addDrawableChild(btn25_36);
        addDrawableChild(btn0);
        addDrawableChild(btn1);
        addDrawableChild(btn2);
        addDrawableChild(btn3);
        addDrawableChild(btn4);
        addDrawableChild(btn5);
        addDrawableChild(btn6);
        addDrawableChild(btn7);
        addDrawableChild(btn8);
        addDrawableChild(btn9);
        addDrawableChild(btn10);
        addDrawableChild(btn11);
        addDrawableChild(btn12);
        addDrawableChild(btn13);
        addDrawableChild(btn14);
        addDrawableChild(btn15);
        addDrawableChild(btn16);
        addDrawableChild(btn17);
        addDrawableChild(btn18);
        addDrawableChild(btn19);
        addDrawableChild(btn20);
        addDrawableChild(btn21);
        addDrawableChild(btn22);
        addDrawableChild(btn23);
        addDrawableChild(btn24);
        addDrawableChild(btn25);
        addDrawableChild(btn26);
        addDrawableChild(btn27);
        addDrawableChild(btn28);
        addDrawableChild(btn29);
        addDrawableChild(btn30);
        addDrawableChild(btn31);
        addDrawableChild(btn32);
        addDrawableChild(btn33);
        addDrawableChild(btn34);
        addDrawableChild(btn35);
        addDrawableChild(btn36);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
        drawChips(context);
        if (handler.lastRoll != null) {
            // "Last Roll" text display
            context.drawText(
                    MinecraftClient.getInstance().textRenderer, "Last Roll:",
                    x + 128, y + 30, 0xFFFFFF, false
            );
            // last roll icon display
            context.drawTexture(
                    Identifier.of(CasinoMod.MOD_ID, handler.lastRoll),
                    x + 140, y + 40, 0, 0, 0,
                    20, 20, 20, 20
            );
        }
    }

    // unfortunately I can't think of a better way to handle this...
    private void drawChips(DrawContext context) {
        if (handler.betMap.get("red") != null) {
            drawScaledChip(context, handler.betMap.get("red"), x + 8, y +  84);
        }
        if (handler.betMap.get("black") != null) {
            drawScaledChip(context, handler.betMap.get("black"), x + 8, y + 108);
        }
        if (handler.betMap.get("1-18") != null) {
            drawScaledChip(context, handler.betMap.get("1-18"), x + 16, y + 65);
        }
        if (handler.betMap.get("19-36") != null) {
            drawScaledChip(context, handler.betMap.get("19-36"), x + 49, y + 65);
        }
        if (handler.betMap.get("1-12") != null) {
            drawScaledChip(context, handler.betMap.get("1-12"), x + 82, y + 65);
        }
        if (handler.betMap.get("13-24") != null) {
            drawScaledChip(context, handler.betMap.get("13-24"), x + 115, y + 65);
        }
        if (handler.betMap.get("25-36") != null) {
            drawScaledChip(context, handler.betMap.get("25-36"), x + 148, y + 65);
        }
        if (handler.betMap.get("0") != null) {
            drawScaledChip(context, handler.betMap.get("0"), x + 8, y + 127);
        }
        if (handler.betMap.get("1") != null) {
            drawScaledChip(context, handler.betMap.get("1"), x + 24, y + 81);
        }
        if (handler.betMap.get("2") != null) {
            drawScaledChip(context, handler.betMap.get("2"), x + 41, y + 81);
        }
        if (handler.betMap.get("3") != null) {
            drawScaledChip(context, handler.betMap.get("3"), x + 57, y + 81);
        }
        if (handler.betMap.get("4") != null) {
            drawScaledChip(context, handler.betMap.get("4"), x + 74, y + 81);
        }
        if (handler.betMap.get("5") != null) {
            drawScaledChip(context, handler.betMap.get("5"), x + 90, y + 81);
        }
        if (handler.betMap.get("6") != null) {
            drawScaledChip(context, handler.betMap.get("6"), x + 107, y + 81);
        }
        if (handler.betMap.get("7") != null) {
            drawScaledChip(context, handler.betMap.get("7"), x + 123, y + 81);
        }
        if (handler.betMap.get("8") != null) {
            drawScaledChip(context, handler.betMap.get("8"), x + 140, y + 81);
        }
        if (handler.betMap.get("9") != null) {
            drawScaledChip(context, handler.betMap.get("9"), x + 156, y + 81);
        }
        if (handler.betMap.get("10") != null) {
            drawScaledChip(context, handler.betMap.get("10"), x + 24, y + 97);
        }
        if (handler.betMap.get("11") != null) {
            drawScaledChip(context, handler.betMap.get("11"), x + 41, y + 97);
        }
        if (handler.betMap.get("12") != null) {
            drawScaledChip(context, handler.betMap.get("12"), x + 57, y + 97);
        }
        if (handler.betMap.get("13") != null) {
            drawScaledChip(context, handler.betMap.get("13"), x + 74, y + 97);
        }
        if (handler.betMap.get("14") != null) {
            drawScaledChip(context, handler.betMap.get("14"), x + 90, y + 97);
        }
        if (handler.betMap.get("15") != null) {
            drawScaledChip(context, handler.betMap.get("15"), x + 107, y + 97);
        }
        if (handler.betMap.get("16") != null) {
            drawScaledChip(context, handler.betMap.get("16"), x + 123, y + 97);
        }
        if (handler.betMap.get("17") != null) {
            drawScaledChip(context, handler.betMap.get("17"), x + 140, y + 97);
        }
        if (handler.betMap.get("18") != null) {
            drawScaledChip(context, handler.betMap.get("18"), x + 156, y + 97);
        }
        if (handler.betMap.get("19") != null) {
            drawScaledChip(context, handler.betMap.get("19"), x + 24, y + 112);
        }
        if (handler.betMap.get("20") != null) {
            drawScaledChip(context, handler.betMap.get("20"), x + 41, y + 112);
        }
        if (handler.betMap.get("21") != null) {
            drawScaledChip(context, handler.betMap.get("21"), x + 57, y + 112);
        }
        if (handler.betMap.get("22") != null) {
            drawScaledChip(context, handler.betMap.get("22"), x + 74, y + 112);
        }
        if (handler.betMap.get("23") != null) {
            drawScaledChip(context, handler.betMap.get("23"), x + 90, y + 112);
        }
        if (handler.betMap.get("24") != null) {
            drawScaledChip(context, handler.betMap.get("24"), x + 107, y + 112);
        }
        if (handler.betMap.get("25") != null) {
            drawScaledChip(context, handler.betMap.get("25"), x + 123, y + 112);
        }
        if (handler.betMap.get("26") != null) {
            drawScaledChip(context, handler.betMap.get("26"), x + 140, y + 112);
        }
        if (handler.betMap.get("27") != null) {
            drawScaledChip(context, handler.betMap.get("27"), x + 156, y + 112);
        }
        if (handler.betMap.get("28") != null) {
            drawScaledChip(context, handler.betMap.get("28"), x + 24, y + 127);
        }
        if (handler.betMap.get("29") != null) {
            drawScaledChip(context, handler.betMap.get("29"), x + 41, y + 127);
        }
        if (handler.betMap.get("30") != null) {
            drawScaledChip(context, handler.betMap.get("30"), x + 57, y + 127);
        }
        if (handler.betMap.get("31") != null) {
            drawScaledChip(context, handler.betMap.get("31"), x + 74, y + 127);
        }
        if (handler.betMap.get("32") != null) {
            drawScaledChip(context, handler.betMap.get("32"), x + 90, y + 127);
        }
        if (handler.betMap.get("33") != null) {
            drawScaledChip(context, handler.betMap.get("33"), x + 107, y + 127);
        }
        if (handler.betMap.get("34") != null) {
            drawScaledChip(context, handler.betMap.get("34"), x + 123, y + 127);
        }
        if (handler.betMap.get("35") != null) {
            drawScaledChip(context, handler.betMap.get("35"), x + 140, y + 127);
        }
        if (handler.betMap.get("36") != null) {
            drawScaledChip(context, handler.betMap.get("36"), x + 156, y + 127);
        }
    }

    // Draws a 75% sized ItemStack at position (x,y)
    private void drawScaledChip(DrawContext context, ItemStack chipStack, int x, int y) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(0.75f, 0.75f, 1.0f);
        context.drawItem(chipStack, 0 ,0);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, chipStack, 0 ,0);
        context.getMatrices().pop();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
