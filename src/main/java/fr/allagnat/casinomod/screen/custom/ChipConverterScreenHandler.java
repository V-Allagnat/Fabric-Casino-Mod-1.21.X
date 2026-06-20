package fr.allagnat.casinomod.screen.custom;

import fr.allagnat.casinomod.block.ModBlocks;
import fr.allagnat.casinomod.block.entity.custom.ChipConverterBlockEntity;
import fr.allagnat.casinomod.item.ModItems;
import fr.allagnat.casinomod.screen.ModScreenHandlers;
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
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class ChipConverterScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PlayerEntity player;
    private final BlockPos entityPos;
    private final Map<Item, Integer> chipsToGive;


    public ChipConverterScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(pos));
    }

    public ChipConverterScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.CHIP_CONVERTER_SCREEN_HANDLER, syncId);
        this.inventory = (Inventory) blockEntity;
        this.player = playerInventory.player;
        this.entityPos = blockEntity.getPos();
        this.chipsToGive = new HashMap<>();

        initializePlayerChips();

        this.addSlot(new Slot(inventory, 0, 8, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                if (stack.isIn(ModTags.Items.CHIP_ITEMS)) {
                    return super.canInsert(stack);
                }
                return false;
            }
        });
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        if (player.getWorld().getBlockEntity(entityPos) instanceof ChipConverterBlockEntity chipConverterBlockEntity) {
            chipConverterBlockEntity.setCurrentUserUUID(null);
        }
        super.onClosed(player);
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public void giveAndDecrementChips(int decrement) {
        for (Map.Entry<Item, Integer> entry : chipsToGive.entrySet()) {
            if (entry.getValue() > 0) {
                ClientPlayNetworking.send(new InventoryAddPayload(new ItemStack(entry.getKey()), entry.getValue()));
                player.getWorld().playSound(player, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 2f, 1f);
            }
        }
        initializePlayerChips();
        decrementConverterStack(decrement);
    }

    public void decrementConverterStack(int amount) {
        // Client Side
        slots.getFirst().getStack().decrement(amount);
        // Server Side
        ClientPlayNetworking.send(new StackDecrementPayload(entityPos, amount));
    }

    public void addChipAmount(Item chipType, int amount) {
        chipsToGive.replace(chipType, chipsToGive.get(chipType) + amount);
    }

    public void convertAndAdd(int toRefund) {
        while (toRefund >= 1000) {
            toRefund -= 1000;
            chipsToGive.replace(ModItems.CHIP_1000, chipsToGive.get(ModItems.CHIP_1000) + 1);
        }
        while (toRefund >= 500) {
            toRefund -= 500;
            chipsToGive.replace(ModItems.CHIP_500, chipsToGive.get(ModItems.CHIP_500) + 1);
        }
        while (toRefund >= 100) {
            toRefund -= 100;
            chipsToGive.replace(ModItems.CHIP_100, chipsToGive.get(ModItems.CHIP_100) + 1);
        }
        while (toRefund >= 50) {
            toRefund -= 50;
            chipsToGive.replace(ModItems.CHIP_50, chipsToGive.get(ModItems.CHIP_50) + 1);
        }
        while (toRefund >= 25) {
            toRefund -= 25;
            chipsToGive.replace(ModItems.CHIP_25, chipsToGive.get(ModItems.CHIP_25) + 1);
        }
        while (toRefund >= 10) {
            toRefund -= 10;
            chipsToGive.replace(ModItems.CHIP_10, chipsToGive.get(ModItems.CHIP_10) + 1);
        }
        while (toRefund >= 5) {
            toRefund -= 5;
            chipsToGive.replace(ModItems.CHIP_5, chipsToGive.get(ModItems.CHIP_5) + 1);
        }
        while (toRefund > 0) {
            toRefund--;
            chipsToGive.replace(ModItems.CHIP_1, chipsToGive.get(ModItems.CHIP_1) + 1);
        }
    }

    private void initializePlayerChips() {
        chipsToGive.clear();
        chipsToGive.put(ModItems.CHIP_1, 0);
        chipsToGive.put(ModItems.CHIP_5, 0);
        chipsToGive.put(ModItems.CHIP_10, 0);
        chipsToGive.put(ModItems.CHIP_25, 0);
        chipsToGive.put(ModItems.CHIP_50, 0);
        chipsToGive.put(ModItems.CHIP_100, 0);
        chipsToGive.put(ModItems.CHIP_500, 0);
        chipsToGive.put(ModItems.CHIP_1000, 0);
    }

    public int getChipsValue() {
        ItemStack chip = slots.getFirst().getStack();
        if (!chip.isIn(ModTags.Items.CHIP_ITEMS)) {
            return 0;
        }
        return ModItems.valueMap.get(chip.getItem()) * chip.getCount();
    }

    // strcmp but for chips
    public int chipCmp(ItemStack stack1, ItemStack stack2) {
        if (!stack1.isIn(ModTags.Items.CHIP_ITEMS) || !stack2.isIn(ModTags.Items.CHIP_ITEMS)) {
            return 0;
        }
        return chipCmp(stack1.getItem(), stack2.getItem());
    }

    public int chipCmp(Item chip1, Item chip2) {
        if (chip1 == ItemStack.EMPTY.getItem() || chip2 == ItemStack.EMPTY.getItem()) {
            return 0;
        }
        return ModItems.valueMap.get(chip1).compareTo(ModItems.valueMap.get(chip2));
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
        return player.getWorld().getBlockState(entityPos).isOf(ModBlocks.CHIP_CONVERTER);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
