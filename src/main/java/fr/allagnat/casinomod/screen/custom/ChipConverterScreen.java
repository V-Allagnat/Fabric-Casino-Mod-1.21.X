package fr.allagnat.casinomod.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.allagnat.casinomod.CasinoMod;
import fr.allagnat.casinomod.item.ModItems;
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

public class ChipConverterScreen extends HandledScreen<ChipConverterScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of(CasinoMod.MOD_ID, "textures/gui/chip_converter/chip_converter_gui.png");

    private final ItemStack chip_1 = new ItemStack(ModItems.CHIP_1, 1);
    private final ItemStack chip_5 = new ItemStack(ModItems.CHIP_5, 5);
    private final ItemStack chip_10 = new ItemStack(ModItems.CHIP_10, 10);
    private final ItemStack chip_25 = new ItemStack(ModItems.CHIP_25, 25);
    private final ItemStack chip_50 = new ItemStack(ModItems.CHIP_50, 50);
    private final ItemStack chip_100 = new ItemStack(ModItems.CHIP_100, 100);
    private final ItemStack chip_500 = new ItemStack(ModItems.CHIP_500, 500);
    private final ItemStack chip_1000 = new ItemStack(ModItems.CHIP_1000, 1000);

    // BUTTONS

    private final ButtonWidget convertTo1s = ButtonWidget.builder(Text.translatable("display.casinomod.chip_converter.trade_button"), button -> {
        if (handler.slots.getFirst().getStack().isEmpty()) {
            return;
        }
        int amount = ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem());
        handler.addChipAmount(ModItems.CHIP_1, amount);
        handler.giveAndDecrementChips(1);
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.chip_converter.convert_to_1s_tooltip")))
            .build();

    private final ButtonWidget convertTo5s = ButtonWidget.builder(Text.translatable("display.casinomod.chip_converter.trade_button"), button -> {
        // check for up- or down-conversion
        int cmp = handler.chipCmp(handler.slots.getFirst().getStack().getItem(), ModItems.CHIP_5);
        if (cmp < 0) { // up-conversion
            if (handler.slots.getFirst().getStack().getCount() < 5) {
                return;
            }
            handler.addChipAmount(ModItems.CHIP_5, 1);
            handler.giveAndDecrementChips(5);
        } else if (cmp > 0) { // down-conversion
            int multiplier = ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem()) / 5;
            handler.addChipAmount(ModItems.CHIP_5, multiplier);
            handler.giveAndDecrementChips(1);
        }
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.chip_converter.convert_to_5s_tooltip")))
            .build();

    private final ButtonWidget convertTo10s = ButtonWidget.builder(Text.translatable("display.casinomod.chip_converter.trade_button"), button -> {
        int cmp = handler.chipCmp(handler.slots.getFirst().getStack().getItem(), ModItems.CHIP_10);
        if (cmp < 0) { // up-conversion
            int amount = 10 / ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem());
            if (handler.slots.getFirst().getStack().getCount() < amount) {
                return;
            }
            handler.addChipAmount(ModItems.CHIP_10, 1);
            handler.giveAndDecrementChips(amount);
        } else if (cmp > 0) { // down-conversion
            if (handler.slots.getFirst().getStack().getItem() != ModItems.CHIP_25) {
                int multiplier = ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem()) / 10;
                handler.addChipAmount(ModItems.CHIP_10, multiplier);
                handler.giveAndDecrementChips(1);
            } else { // edge case for chip 25 since you can't get an integer from 25 / 10
                handler.addChipAmount(ModItems.CHIP_10, 2);
                handler.addChipAmount(ModItems.CHIP_5, 1);
                handler.giveAndDecrementChips(1);
            }
        }
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.chip_converter.convert_to_10s_tooltip")))
            .build();

    private final ButtonWidget convertTo25s = ButtonWidget.builder(Text.translatable("display.casinomod.chip_converter.trade_button"), button -> {
        int cmp = handler.chipCmp(handler.slots.getFirst().getStack().getItem(), ModItems.CHIP_25);
        if (cmp < 0) { // up-conversion
            if (handler.slots.getFirst().getStack().getItem() == ModItems.CHIP_10) {
                if (handler.slots.getFirst().getStack().getCount() < 3) {
                    return;
                }
                handler.addChipAmount(ModItems.CHIP_25, 1);
                handler.addChipAmount(ModItems.CHIP_5, 1);
                handler.giveAndDecrementChips(3);
                return;
            }
            int amount = 25 / ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem());
            if (handler.slots.getFirst().getStack().getCount() < amount) {
                return;
            }
            handler.addChipAmount(ModItems.CHIP_25, 1);
            handler.giveAndDecrementChips(amount);
        } else if (cmp > 0) { // down-conversion
            int multiplier = ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem()) / 25;
            handler.addChipAmount(ModItems.CHIP_25, multiplier);
            handler.giveAndDecrementChips(1);
        }
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.chip_converter.convert_to_25s_tooltip")))
            .build();

    private final ButtonWidget convertTo50s = ButtonWidget.builder(Text.translatable("display.casinomod.chip_converter.trade_button"), button -> {
                int cmp = handler.chipCmp(handler.slots.getFirst().getStack().getItem(), ModItems.CHIP_50);
                if (cmp < 0) { // up-conversion
                    int amount = 50 / ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem());
                    if (handler.slots.getFirst().getStack().getCount() < amount) {
                        return;
                    }
                    handler.addChipAmount(ModItems.CHIP_50, 1);
                    handler.giveAndDecrementChips(amount);
                } else if (cmp > 0) { // down-conversion
                    int multiplier = ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem()) / 50;
                    handler.addChipAmount(ModItems.CHIP_50, multiplier);
                    handler.giveAndDecrementChips(1);
                }
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.chip_converter.convert_to_50s_tooltip")))
            .build();

    private final ButtonWidget convertTo100s = ButtonWidget.builder(Text.translatable("display.casinomod.chip_converter.trade_button"), button -> {
        int cmp = handler.chipCmp(handler.slots.getFirst().getStack().getItem(), ModItems.CHIP_100);
        if (cmp < 0) { // up-conversion
            int amount = 100 / ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem());
            if (handler.slots.getFirst().getStack().getCount() < amount) {
                return;
            }
            handler.addChipAmount(ModItems.CHIP_100, 1);
            handler.giveAndDecrementChips(amount);
        } else if (cmp > 0) { // down-conversion
            int multiplier = ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem()) / 100;
            handler.addChipAmount(ModItems.CHIP_100, multiplier);
            handler.giveAndDecrementChips(1);
        }
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.chip_converter.convert_to_100s_tooltip")))
            .build();

    private final ButtonWidget convertTo500s = ButtonWidget.builder(Text.translatable("display.casinomod.chip_converter.trade_button"), button -> {
        int cmp = handler.chipCmp(handler.slots.getFirst().getStack().getItem(), ModItems.CHIP_500);
        if (cmp < 0) { // up-conversion
            int amount = 500 / ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem());
            if (handler.slots.getFirst().getStack().getCount() < amount) {
                return;
            }
            handler.addChipAmount(ModItems.CHIP_500, 1);
            handler.giveAndDecrementChips(amount);
        } else if (cmp > 0) { // down-conversion
            int multiplier = ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem()) / 500;
            handler.addChipAmount(ModItems.CHIP_500, multiplier);
            handler.giveAndDecrementChips(1);
        }
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.chip_converter.convert_to_500s_tooltip")))
            .build();

    private final ButtonWidget convertTo1000s = ButtonWidget.builder(Text.translatable("display.casinomod.chip_converter.trade_button"), button -> {
                int cmp = handler.chipCmp(handler.slots.getFirst().getStack().getItem(), ModItems.CHIP_1000);
                if (cmp < 0) { // up-conversion
                    int amount = 1000 / ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem());
                    if (handler.slots.getFirst().getStack().getCount() < amount) {
                        return;
                    }
                    handler.addChipAmount(ModItems.CHIP_1000, 1);
                    handler.giveAndDecrementChips(amount);
                } else if (cmp > 0) { // down-conversion
                    int multiplier = ModItems.valueMap.get(handler.slots.getFirst().getStack().getItem()) / 1000;
                    handler.addChipAmount(ModItems.CHIP_1000, multiplier);
                    handler.giveAndDecrementChips(1);
                }
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.chip_converter.convert_to_1000s_tooltip")))
            .build();

    // ! BUTTONS

    public ChipConverterScreen(ChipConverterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        // get the 'Inventory' label way out of the screen
        playerInventoryTitleX += 999999;
        playerInventoryTitleY += 999999;

        convertTo1s.active = true;
        convertTo5s.active = true;
        convertTo10s.active = true;
        convertTo25s.active = true;
        convertTo50s.active = true;
        convertTo100s.active = true;
        convertTo500s.active = true;
        convertTo1000s.active = true;

        convertTo1s.setDimensionsAndPosition(40, 16, x + 50, y + 10 - 3 + 1);
        convertTo5s.setDimensionsAndPosition(40, 16, x + 50, y + 25 - 1 + 1);
        convertTo10s.setDimensionsAndPosition(40, 16, x + 50, y + 40 + 1 + 1);
        convertTo25s.setDimensionsAndPosition(40, 16, x + 50, y + 55 + 3 + 1);
        convertTo50s.setDimensionsAndPosition(40, 16, x + 118, y + 10 - 3 + 1);
        convertTo100s.setDimensionsAndPosition(40, 16, x + 118, y + 25 - 1 + 1);
        convertTo500s.setDimensionsAndPosition(40, 16, x + 118, y + 40 + 1 + 1);
        convertTo1000s.setDimensionsAndPosition(40, 16, x + 118, y + 55 + 3 + 1);

        addDrawableChild(convertTo1s);
        addDrawableChild(convertTo5s);
        addDrawableChild(convertTo10s);
        addDrawableChild(convertTo25s);
        addDrawableChild(convertTo50s);
        addDrawableChild(convertTo100s);
        addDrawableChild(convertTo500s);
        addDrawableChild(convertTo1000s);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        context.drawItem(chip_1, x + 32, y + 10 - 3);
        context.drawItem(chip_5, x + 32, y + 25 - 1);
        context.drawItem(chip_10, x + 32, y + 40 + 1);
        context.drawItem(chip_25, x + 32, y + 55 + 3);
        context.drawItem(chip_50, x + 100, y + 10 - 3);
        context.drawItem(chip_100, x + 100, y + 25 - 1);
        context.drawItem(chip_500, x + 100, y + 40 + 1);
        context.drawItem(chip_1000, x + 100, y + 55 + 3);

        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, chip_1, x + 32, y + 10 - 3);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, chip_5, x + 32, y + 25 - 1);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, chip_10, x + 32, y + 40 + 1);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, chip_25, x + 32, y + 55 + 3);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, chip_50, x + 100, y + 10 - 3);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, chip_100, x + 100, y + 25 - 1);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, chip_500, x + 100, y + 40 + 1);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, chip_1000, x + 100, y + 55 + 3);

        // DRAW TOOLTIP ON MOUSE HOVER

        if (isPointWithinBounds(32, 10 - 3, 16, 16, mouseX, mouseY)) {
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, chip_1, mouseX, mouseY);
        }
        if (isPointWithinBounds(32, 25 - 1, 16, 16, mouseX, mouseY)) {
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, chip_5, mouseX, mouseY);
        }
        if (isPointWithinBounds(32, 40 + 1, 16, 16, mouseX, mouseY)) {
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, chip_10, mouseX, mouseY);
        }
        if (isPointWithinBounds(32, 55 + 3, 16, 16, mouseX, mouseY)) {
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, chip_25, mouseX, mouseY);
        }
        if (isPointWithinBounds(100, 10 - 3, 16, 16, mouseX, mouseY)) {
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, chip_50, mouseX, mouseY);
        }
        if (isPointWithinBounds(100, 25 - 1, 16, 16, mouseX, mouseY)) {
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, chip_100, mouseX, mouseY);
        }
        if (isPointWithinBounds(100, 40 + 1, 16, 16, mouseX, mouseY)) {
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, chip_500, mouseX, mouseY);
        }
        if (isPointWithinBounds(100, 55 + 3, 16, 16, mouseX, mouseY)) {
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, chip_1000, mouseX, mouseY);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
