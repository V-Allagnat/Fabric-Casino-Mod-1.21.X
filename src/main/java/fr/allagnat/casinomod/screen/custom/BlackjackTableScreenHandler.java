package fr.allagnat.casinomod.screen.custom;

import fr.allagnat.casinomod.block.ModBlocks;
import fr.allagnat.casinomod.block.entity.custom.BlackjackTableBlockEntity;
import fr.allagnat.casinomod.block.entity.custom.RouletteBlockEntity;
import fr.allagnat.casinomod.item.ModItems;
import fr.allagnat.casinomod.screen.ModScreenHandlers;
import fr.allagnat.casinomod.util.BlackjackCards;
import fr.allagnat.casinomod.util.ModTags;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackjackTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PlayerEntity player;
    private final BlockEntity blockEntity;
    private final BlockPos entityPos;
    private final List<Pair<String, Point>> cardsToDraw;
    private final List<Pair<String, Point>> dealerCardsToDraw;
    private final Map<Item, Integer> playerReward;

    public int currentBet;
    public Item chipType;
    public int currentPoints;
    public int dealerPoints;
    // These 2 fields are used to correctly keep track of points when aces are in play
    public int acesWorth11;
    public int dealerAcesWorth11;
    // A class attribute is needed to keep track of when to reveal and draw the card
    private Pair<String, Integer> dealerHiddenCard;
    private final BlackjackCards cards;

    public PlayerEntity getPlayer() {
        return player;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public BlockPos getEntityPos() {
        return entityPos;
    }

    public BlackjackCards getCards() {
        return cards;
    }

    public List<Pair<String, Point>> getCardsToDraw() {
        return cardsToDraw;
    }

    public List<Pair<String, Point>> getDealerCardsToDraw() {
        return dealerCardsToDraw;
    }

    public BlackjackTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(pos));
    }

    public BlackjackTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.BLACKJACK_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = ((Inventory) blockEntity);
        this.player = playerInventory.player;
        this.blockEntity = blockEntity;
        this.entityPos = blockEntity.getPos();
        this.cardsToDraw = new ArrayList<>();
        this.dealerCardsToDraw = new ArrayList<>();
        this.playerReward = new HashMap<>();
        initializePlayerReward();

        this.dealerHiddenCard = null;
        this.cards = new BlackjackCards();

        this.addSlot(new Slot(inventory, 0, 7, 7) {
            @Override
            public boolean canInsert(ItemStack stack) {
                if (stack.isIn(ModTags.Items.CHIP_ITEMS)) {
                    return super.canInsert(stack);
                }
                return false;
            }
        });
        addPlayerHotbar(playerInventory);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        if (player.getWorld().getBlockEntity(entityPos) instanceof BlackjackTableBlockEntity blackjackTableBlockEntity) {
            System.out.println("current user UUID: " + blackjackTableBlockEntity.getCurrentUserUUID());
            blackjackTableBlockEntity.setCurrentUserUUID(null);
            System.out.println("after closing interface: " + blackjackTableBlockEntity.getCurrentUserUUID());
        }
        super.onClosed(player);
    }

    public void setAndGiveReward(int value) {
        setPlayerReward(value);
        givePlayerReward();
    }

    public void setPlayerReward(int value) {
        while (value >= 1000) {
            value -= 1000;
            playerReward.replace(ModItems.CHIP_1000, playerReward.get(ModItems.CHIP_1000) + 1);
        }
        while (value >= 500) {
            value -= 500;
            playerReward.replace(ModItems.CHIP_500, playerReward.get(ModItems.CHIP_500) + 1);
        }
        while (value >= 100) {
            value -= 100;
            playerReward.replace(ModItems.CHIP_100, playerReward.get(ModItems.CHIP_100) + 1);
        }
        while (value >= 50) {
            value -= 50;
            playerReward.replace(ModItems.CHIP_50, playerReward.get(ModItems.CHIP_50) + 1);
        }
        while (value >= 25) {
            value -= 25;
            playerReward.replace(ModItems.CHIP_25, playerReward.get(ModItems.CHIP_25) + 1);
        }
        while (value >= 10) {
            value -= 10;
            playerReward.replace(ModItems.CHIP_10, playerReward.get(ModItems.CHIP_10) + 1);
        }
        while (value >= 5) {
            value -= 5;
            playerReward.replace(ModItems.CHIP_5, playerReward.get(ModItems.CHIP_5) + 1);
        }
        while (value > 0) {
            value--;
            playerReward.replace(ModItems.CHIP_1, playerReward.get(ModItems.CHIP_1) + 1);
        }
    }

    public void givePlayerReward() {
        for (Map.Entry<Item, Integer> entry : playerReward.entrySet()) {
            if (entry.getValue() > 0) {
                ClientPlayNetworking.send(new InventoryAddPayload(new ItemStack(entry.getKey()), entry.getValue()));
                player.getWorld().playSound(player, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 2f, 1f);
            }
        }
        initializePlayerReward();
    }

    private void initializePlayerReward() {
        playerReward.clear();
        playerReward.put(ModItems.CHIP_1, 0);
        playerReward.put(ModItems.CHIP_5, 0);
        playerReward.put(ModItems.CHIP_10, 0);
        playerReward.put(ModItems.CHIP_25, 0);
        playerReward.put(ModItems.CHIP_50, 0);
        playerReward.put(ModItems.CHIP_100, 0);
        playerReward.put(ModItems.CHIP_500, 0);
        playerReward.put(ModItems.CHIP_1000, 0);
    }

    public void playDealersTurn() {
//        player.sendMessage(Text.translatable("message.casinomod.blackjack_table.dealers_turn"));
        StringBuilder drawnCards = new StringBuilder();
        revealHiddenCard();
        while (dealerPoints < 17) {
            Pair<String, Integer> card = cards.drawCard();
            addToDealerScore(card);
            addDealerCardToDraw(card.getLeft());
            if (!drawnCards.isEmpty()) {
                drawnCards.append(", ").append(getFormatted(card.getLeft()));
            } else {
                drawnCards.append("  ").append(getFormatted(card.getLeft()));
            }
        }
//        if (!drawnCards.isEmpty()) {
//            player.sendMessage(Text.literal(drawnCards.toString()));
//        }
//        player.sendMessage(Text.translatable("message.casinomod.blackjack_table.dealers_final_score",
//                "§a" + dealerPoints + "§r"
//        ));
    }

    public void revealHiddenCard() {
        if (dealerCardsToDraw.getFirst().getLeft().equals("?")) {
            dealerCardsToDraw.getFirst().setLeft(dealerHiddenCard.getLeft());
            addToDealerScore(dealerHiddenCard);
//            player.sendMessage(Text.translatable("message.casinomod.blackjack_table.dealers_hidden_card",
//                getFormatted(dealerHiddenCard.getLeft()), "§a" + dealerPoints + "§r"
//            ));
        }
    }

    public void addCardToDraw(String card) {
        if (cardsToDraw.isEmpty()) {
            cardsToDraw.add(new Pair<>(card, new Point(0, 0)));
            return;
        }
        Point previous = cardsToDraw.getLast().getRight();
        cardsToDraw.add(new Pair<>(card, new Point(0, (int) (previous.getY() + (double) BlackjackTableScreen.cardHeight / 3))));
    }

    public void addDealerCardToDraw(String card) {
        if (dealerCardsToDraw.isEmpty()) {
            dealerCardsToDraw.add(new Pair<>(card, new Point(0, 0)));
            return;
        }
        Point previous = dealerCardsToDraw.getLast().getRight();
        dealerCardsToDraw.add(new Pair<>(card, new Point(0, (int) (previous.getY() + (double) BlackjackTableScreen.cardHeight / 3))));
    }

    public void initializeRound() {
        currentPoints = 0;
        dealerPoints = 0;
        acesWorth11 = 0;
        dealerAcesWorth11 = 0;
        cardsToDraw.clear();
        dealerCardsToDraw.clear();


        // Draw initial hands
        Pair<String, Integer> firstCard = getCards().drawCard();
        dealerHiddenCard = getCards().drawCard();
        Pair<String, Integer> secondCard = getCards().drawCard();
        Pair<String, Integer> dealerCard = getCards().drawCard();

        addCardToDraw(firstCard.getLeft());
        addDealerCardToDraw("?");
        addCardToDraw(secondCard.getLeft());
        addDealerCardToDraw(dealerCard.getLeft());

        addToScore(firstCard);
        addToScore(secondCard);
        addToDealerScore(dealerCard);

        player.sendMessage(Text.translatable("message.casinomod.blackjack_table.initialize_round",
                getFormatted(firstCard.getLeft()), getFormatted(secondCard.getLeft()), "§a" + currentPoints + "§r",
                getFormatted(dealerCard.getLeft()), "§a" + dealerPoints + "§r"
        ));
    }

    public void addToScore(Pair<String, Integer> card) {
        if (card.getRight() == 1) {
            // Ace
            currentPoints += 10;
            acesWorth11++;
        }
        currentPoints += card.getRight();
        while (currentPoints > 21) {
            if (acesWorth11 == 0) {
                // Bust
                return;
            }
            currentPoints -= 10;
            acesWorth11--;
        }
    }

    public void addToDealerScore(Pair<String, Integer> card) {
        if (card.getRight() == 1) {
            // Ace
            dealerPoints += 10;
            dealerAcesWorth11++;
        }
        dealerPoints += card.getRight();
        while (dealerPoints > 21) {
            if (dealerAcesWorth11 == 0) {
                // Bust
                return;
            }
            dealerPoints -= 10;
            dealerAcesWorth11--;
        }
    }

    //   /!\ WARNING /!\   ONLY THIS METHOD VARIANT modifies the class attribute 'chipType'
    public int getBetValue() {
        ItemStack chip = slots.getFirst().getStack();
        if (!chip.isIn(ModTags.Items.CHIP_ITEMS)) {
            return 0;
        }
        chipType = chip.getItem();

        return getBetValue(chipType);
    }

    public int getBetValue(Item chipItem) {
        if (!new ItemStack(chipItem).isIn(ModTags.Items.CHIP_ITEMS)) {
            return -1;
        }
        return ModItems.valueMap.get(chipItem) * currentBet;
    }

    public String getFormatted(String card) {
        if (card == null || card.isEmpty()) {
            return card;
        }
        return switch (card.charAt(card.length() - 1)) {
            case '♥', '♦' -> "§c" + card + "§r";
            case '♣', '♠' -> "§8" + card + "§r";
            default -> card;
        };
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack() && slot.getStack().isIn(ModTags.Items.CHIP_ITEMS)) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getWorld().getBlockState(entityPos).isOf(ModBlocks.BLACKJACK_TABLE);
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
