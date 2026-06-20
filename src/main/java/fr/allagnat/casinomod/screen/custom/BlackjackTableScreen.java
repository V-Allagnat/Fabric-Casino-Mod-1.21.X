package fr.allagnat.casinomod.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.allagnat.casinomod.CasinoMod;
import fr.allagnat.casinomod.block.entity.custom.BlackjackTableBlockEntity;
import fr.allagnat.casinomod.util.BlackjackCards;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.awt.*;

public class BlackjackTableScreen extends HandledScreen<BlackjackTableScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of(CasinoMod.MOD_ID, "textures/gui/blackjack_table/blackjack_table_gui.png");

    // Dimensions to draw, conventional ratio is 2.5"/3.5"
    public static final int cardSize = 5;
    public static final int cardWidth = cardSize * 5; // 2.5 * 2 to get an integer
    public static final int cardHeight = cardSize * 7; // 3.5 * 2

    // BUTTONS
    private final ButtonWidget button_hit = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.hit"), button -> {
        button_double_active = false;
        button_surrender_active = false;
        button_split_active = false;

        Pair<String, Integer> card = handler.getCards().drawCard();
        handler.addToScore(card);
        handler.addCardToDraw(card.getLeft());

//        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.hit",
//                handler.getFormatted(card.getLeft()),
//                "§a" + handler.currentPoints + "§r"
//        ));

        if (handler.currentPoints > 21) {
            handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.bust"));
            handler.revealHiddenCard();
            button_hit_active = false;
            button_stand_active = false;
            button_start_active = true;
            button_bet1_active = true;
            button_bet5_active = true;
            button_allin_active = true;
            button_reset_active = true;
            handler.currentBet = 0;
        }
        updateButtons();
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.hit_tooltip")))
            .build();
    private boolean button_hit_active = false;



    private final ButtonWidget button_stand = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.stand"), button -> {
                button_hit_active = false;
                button_stand_active = false;
                button_double_active = false;
                button_surrender_active = false;
                button_split_active = false;
                updateButtons();
                if (handler.currentPoints == 21 && handler.getCardsToDraw().size() == 2) { // blackjack, starting hand has 10 + Ace
                    handler.revealHiddenCard();
                    if (handler.dealerPoints == 21) {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.push"));
                        handler.setAndGiveReward(handler.getBetValue(handler.chipType) * 2);
                    } else {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.player_win"));
                        handler.setAndGiveReward((int) (handler.getBetValue(handler.chipType) * 2.5));
                    }
                } else {
                    handler.playDealersTurn();
                    if (handler.dealerPoints > 21) {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.dealer_bust"));
                        handler.setAndGiveReward(handler.getBetValue(handler.chipType) * 2);
                    } else if (handler.currentPoints > handler.dealerPoints) {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.player_win"));
                        handler.setAndGiveReward(handler.getBetValue(handler.chipType) * 2);
                    } else if (handler.currentPoints < handler.dealerPoints || (handler.dealerPoints == 21 && handler.getDealerCardsToDraw().size() == 2)) {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.dealer_win"));
                    } else {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.push"));
                        handler.setAndGiveReward(handler.getBetValue(handler.chipType));
                    }
                }
                handler.currentBet = 0;
                button_start_active = true;
                button_bet1_active = true;
                button_bet5_active = true;
                button_allin_active = true;
                button_reset_active = true;
                updateButtons();
            }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.stand_tooltip")))
            .build();
    private boolean button_stand_active = false;



    private final ButtonWidget button_double = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.double"), button -> {
                System.out.println("in slot: " + handler.slots.getFirst().getStack().getItem());
                System.out.println("type: " + handler.chipType);

                if (handler.slots.getFirst().getStack().getItem() != handler.chipType) {
                    handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.double_down_different_chips"));
                    return;
                }
                if (handler.slots.getFirst().getStack().getCount() < handler.currentBet
                        || handler.slots.getFirst().getStack().getItem() != handler.chipType) { // cant double down
                    handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.double_down_fail"));
                    return;
                }

                // Client side
                handler.slots.getFirst().getStack().decrement(handler.currentBet);
                // Server side
                ClientPlayNetworking.send(new StackDecrementPayload(handler.getEntityPos(), handler.currentBet));
                handler.getBlockEntity().markDirty();
                handler.getPlayer().getWorld().updateListeners(
                        handler.getEntityPos(),
                        handler.getBlockEntity().getCachedState(),
                        handler.getBlockEntity().getCachedState(),
                        Block.NOTIFY_ALL
                );

                handler.currentBet *= 2;

                button_hit_active = false;
                button_stand_active = false;
                button_double_active = false;
                button_surrender_active = false;
                button_split_active = false;
                Pair<String, Integer> card = handler.getCards().drawCard();
                handler.addToScore(card);
                handler.addCardToDraw(card.getLeft());
                handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.double_down",
                        handler.getFormatted(card.getLeft()),
                        "§a" + handler.currentPoints + "§r"
                ));
                if (handler.currentPoints > 21) {
                    handler.revealHiddenCard();
                    handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.bust"));
                } else {
                    handler.playDealersTurn();
                }
                if (handler.currentPoints <= 21) {
                    if (handler.dealerPoints > 21) {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.dealer_bust"));
                        handler.setAndGiveReward(handler.getBetValue(handler.chipType) * 2);
                    } else if (handler.currentPoints > handler.dealerPoints) {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.player_win"));
                        handler.setAndGiveReward(handler.getBetValue(handler.chipType) * 2);
                    } else if (handler.currentPoints < handler.dealerPoints) {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.dealer_win"));
                    } else {
                        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.push"));
                        handler.setAndGiveReward(handler.getBetValue(handler.chipType));
                    }
                }
                handler.currentBet = 0;
                button_start_active = true;
                button_bet1_active = true;
                button_bet5_active = true;
                button_allin_active = true;
                button_reset_active = true;
                updateButtons();
            }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.double_tooltip")))
            .build();
    private boolean button_double_active = false;



    private final ButtonWidget button_surrender = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.surrender"), button -> {
                button_hit_active = false;
                button_stand_active = false;
                button_double_active = false;
                button_surrender_active = false;
                button_split_active = false;
                button_start_active = true;
                button_bet1_active = true;
                button_bet5_active = true;
                button_allin_active = true;
                button_reset_active = true;
                handler.revealHiddenCard();
                handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.surrender"));
                updateButtons();
                handler.setAndGiveReward(handler.getBetValue(handler.chipType) / 2);
                handler.currentBet = 0;
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.surrender_tooltip")))
            .build();
    private boolean button_surrender_active = false;



    private final ButtonWidget button_split = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.split"), button -> {
                handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.split"));
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.split_tooltip")))
            .build();
    private boolean button_split_active = false;



    private final ButtonWidget button_bet1 = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.bet1"), button -> {
                if (handler.currentBet == handler.slots.getFirst().getStack().getCount()) {
//                    handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.max_bet"));
                    return;
                }
                handler.currentBet++;
//                handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.bet1",
//                        "§a" + handler.currentBet + "§r"
//                ));
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.bet1_tooltip")))
            .build();
    private boolean button_bet1_active = true;



    private final ButtonWidget button_bet5 = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.bet5"), button -> {
                if (handler.currentBet == handler.slots.getFirst().getStack().getCount()) {
//                    handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.max_bet"));
                    return;
                }
                int toAdd = Math.min(handler.slots.getFirst().getStack().getCount() - handler.currentBet, 5);
                handler.currentBet += toAdd;
//                handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.bet5",
//                        "§a" + toAdd + "§r",
//                        "§a" + handler.currentBet + "§r"
//                ));
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.bet5_tooltip")))
            .build();
    private boolean button_bet5_active = true;



    private final ButtonWidget button_allin = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.allin"), button -> {
                if (handler.slots.getFirst().getStack().isEmpty() || handler.slots.getFirst().getStack().getCount() == handler.currentBet) {
//                    handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.max_bet"));
                    return;
                }
                handler.currentBet = handler.slots.getFirst().getStack().getCount();
//                handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.allin",
//                        "§a" + handler.currentBet + "§r"
//                ));
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.allin_tooltip")))
            .build();
    private boolean button_allin_active = true;



    private final ButtonWidget button_reset = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.reset"), button -> {
                handler.currentBet = 0;
//                handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.reset"));
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.reset_tooltip")))
            .build();
    private boolean button_reset_active = true;



    private final ButtonWidget button_start = ButtonWidget.builder(Text.translatable("display.casinomod.blackjack_table.start_round"), button -> {

        // This line is here to prevent players from betting chips, then taking them away from the slot before starting.
        handler.currentBet = Math.min(handler.currentBet,  handler.slots.getFirst().getStack().getCount());

        if (handler.currentBet == 0) {
            handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.start_fail"));
            return;
        }

        handler.getPlayer().sendMessage(Text.translatable("message.casinomod.blackjack_table.start",
                "§a" + handler.currentBet + "§r",
                "§a" + handler.getBetValue() + "§r"
        ));

        // Client side
        handler.slots.getFirst().getStack().decrement(handler.currentBet);
        // Server side
        ClientPlayNetworking.send(new StackDecrementPayload(handler.getEntityPos(), handler.currentBet));

        handler.getBlockEntity().markDirty();
        handler.getPlayer().getWorld().updateListeners(
                handler.getEntityPos(),
                handler.getBlockEntity().getCachedState(),
                handler.getBlockEntity().getCachedState(),
                Block.NOTIFY_ALL
        );

        handler.initializeRound();

        button_start_active = false;
        button_bet1_active = false;
        button_bet5_active = false;
        button_allin_active = false;
        button_reset_active = false;

        button_hit_active = true;
        button_stand_active = true;
        button_double_active = true;
        button_surrender_active = true;
        button_split_active = true;
        updateButtons();
    }).tooltip(Tooltip.of(Text.translatable("display.casinomod.blackjack_table.start_round_tooltip")))
            .build();
    private boolean button_start_active = true;



    public BlackjackTableScreen(BlackjackTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        updateButtons();
        handler.currentBet = 0;
    }

    private void updateButtons() {
        button_hit.active = button_hit_active;
        button_stand.active = button_stand_active;
        button_double.active = button_double_active;
        button_surrender.active = button_surrender_active;
        button_split.active = button_split_active;

        button_bet1.active = button_bet1_active;
        button_bet5.active = button_bet5_active;
        button_allin.active = button_allin_active;
        button_reset.active = button_reset_active;

        button_start.active = button_start_active;
    }

    @Override
    protected void init() {
        super.init();

        // get the 'Inventory' label way out of the screen
        playerInventoryTitleX += 999999;
        playerInventoryTitleY += 999999;

        button_hit.setDimensionsAndPosition(80, 20, x + 186, y + 46);
        button_stand.setDimensionsAndPosition(80, 20, x + 186, y + 71);
        button_double.setDimensionsAndPosition(80, 20, x + 186, y + 96);
        button_surrender.setDimensionsAndPosition(80, 20, x + 186, y + 121);
        button_split.setDimensionsAndPosition(80, 20, x + 186, y + 146);

        button_bet1.setDimensionsAndPosition(80, 20, x - 90, y + 30);
        button_bet5.setDimensionsAndPosition(80, 20, x - 90, y + 50);
        button_allin.setDimensionsAndPosition(80, 20, x - 90, y + 70);
        button_reset.setDimensionsAndPosition(80, 20, x - 90, y + 100);
        button_start.setDimensionsAndPosition(80, 20, x - 90, y);

        addDrawableChild(button_hit);
        addDrawableChild(button_stand);
        addDrawableChild(button_double);
        addDrawableChild(button_surrender);
        addDrawableChild(button_split);

        addDrawableChild(button_bet1);
        addDrawableChild(button_bet5);
        addDrawableChild(button_allin);
        addDrawableChild(button_reset);
        addDrawableChild(button_start);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        try {
            System.out.println("UUID while open: " + ((BlackjackTableBlockEntity) handler.getPlayer().getWorld().getBlockEntity(handler.getEntityPos())).getCurrentUserUUID());
        } catch (NullPointerException e) {
            System.out.println("nullptr...");
        }
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Draw deck
        if (!handler.getCardsToDraw().isEmpty()) {
            context.drawTexture(Identifier.of(CasinoMod.MOD_ID, "textures/gui/blackjack_table/card_back.png"),
                    x + 22, y + 62, 0, 0, 0,
                    cardWidth, cardHeight,
                    cardWidth, cardHeight
            );
        }

        // Draw cards
        if (!handler.getCardsToDraw().isEmpty()) { // or any condition to check if the game has started
            context.drawTexture(Identifier.of(CasinoMod.MOD_ID, "textures/gui/blackjack_table/this_is_a_line.png"),
                    x + 116, y + 10, 0, 0, 0,
                    1, 119,
                    1, 119
            );
        }

        for (Pair<String, Point> card : handler.getCardsToDraw()) {
            final int cardX = 78;
            final int xOffset = (int) card.getRight().getX();
            final int cardY = 15;
            final int yOffset = (int) card.getRight().getY();
            context.drawTexture(Identifier.of(CasinoMod.MOD_ID, BlackjackCards.LOOKUP_PATHS.get(card.getLeft())),
                    x + cardX + xOffset,  y + cardY + yOffset, 0, 0, 0,
                    cardWidth, cardHeight,
                    cardWidth, cardHeight
            );
        }

        for (Pair<String, Point> card : handler.getDealerCardsToDraw()) {
            final int cardX = 129;
            final int xOffset = (int) card.getRight().getX();
            final int cardY = 15;
            final int yOffset = (int) card.getRight().getY();
            context.drawTexture(Identifier.of(CasinoMod.MOD_ID, BlackjackCards.LOOKUP_PATHS.get(card.getLeft())),
                    x + cardX + xOffset, y + cardY + yOffset, 0, 0, 0,
                    cardWidth, cardHeight,
                    cardWidth, cardHeight
            );
        }

        // Define variables depending on the context
        String playerText = handler.currentPoints == 0 ? "" : "points";
        String dealerText = handler.dealerPoints == 0 ? "" : "dealer";
        String playerPointsText = handler.currentPoints == 0 ? "" : Integer.toString(handler.currentPoints);
        String dealerPointsText = handler.dealerPoints == 0 ? "" : Integer.toString(handler.dealerPoints);
        int playerColor = handler.currentPoints == 21 ? 0x00AA00 : (handler.currentPoints > 21 ? 0xAA0000 : 0x555555);
        int dealerColor = handler.dealerPoints == 21 ? 0x00AA00 : (handler.dealerPoints > 21 ? 0xAA0000 : 0x555555);
        int playerPointsOffset = handler.currentPoints < 10 ? 13 : 9;
        int dealerPointsOffset = handler.dealerPoints < 10 ? 13 : 9;

        // Draw bet
        context.drawText(MinecraftClient.getInstance().textRenderer,
                "bet: " + handler.currentBet, x + 7, y + 30, 0x555555, false
        );
        if (handler.chipType == null) {
            context.drawText(MinecraftClient.getInstance().textRenderer,
                    "(" + handler.getBetValue() + ")", x + 7, y + 40, 0x555555, false
            );
        } else {
            context.drawText(MinecraftClient.getInstance().textRenderer,
                    "(" + handler.getBetValue(handler.chipType) + ")", x + 7, y + 40, 0x555555, false
            );
        }


        // Draw points
        context.drawText(MinecraftClient.getInstance().textRenderer,
                playerText, x + 75, y + 120, playerColor, false
        );
        context.drawText(MinecraftClient.getInstance().textRenderer,
                playerPointsText, x + 75 + playerPointsOffset, y + 130, playerColor, false
        );

        context.drawText(MinecraftClient.getInstance().textRenderer,
                dealerText, x + 125, y + 120, dealerColor, false
        );
        context.drawText(MinecraftClient.getInstance().textRenderer,
                dealerPointsText, x + 125 + dealerPointsOffset, y + 130, dealerColor, false
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
