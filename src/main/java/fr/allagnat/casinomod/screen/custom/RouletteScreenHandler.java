package fr.allagnat.casinomod.screen.custom;

import fr.allagnat.casinomod.block.ModBlocks;
import fr.allagnat.casinomod.block.entity.custom.RouletteBlockEntity;
import fr.allagnat.casinomod.screen.ModScreenHandlers;
import fr.allagnat.casinomod.util.ModTags;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;


public class RouletteScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final BlockPos entityPos;

    public RouletteScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(pos));
    }

    public RouletteScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.ROULETTE_SCREEN_HANDLER, syncId);
        this.inventory = (Inventory) blockEntity;
        this.entityPos = blockEntity.getPos();

        this.addSlot(new Slot(inventory, 0, 22, 26) {
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

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        this.addSlot(new Slot(playerInventory, 0, 6, 143 ));
        this.addSlot(new Slot(playerInventory, 1, 6 + 18, 143 ));
        this.addSlot(new Slot(playerInventory, 2, 6 + 2 * 18 + 1, 143 ));
        this.addSlot(new Slot(playerInventory, 3, 6 + 3 * 18 + 2, 143 ));
        this.addSlot(new Slot(playerInventory, 4, 6 + 4 * 18 + 2, 143 ));
        this.addSlot(new Slot(playerInventory, 5, 6 + 5 * 18 + 3, 143 ));
        this.addSlot(new Slot(playerInventory, 6, 6 + 6 * 18 + 3, 143 ));
        this.addSlot(new Slot(playerInventory, 7, 6 + 7 * 18 + 4, 143 ));
        this.addSlot(new Slot(playerInventory, 8, 6 + 8 * 18 + 4, 143 ));
    }

    @Override
    public void onClosed(PlayerEntity player) {
        if (player.getWorld().getBlockEntity(entityPos) instanceof RouletteBlockEntity rouletteBlockEntity) {
            rouletteBlockEntity.setCurrentUserUUID(null);
        }
        super.onClosed(player);
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
        return player.getWorld().getBlockState(entityPos).isOf(ModBlocks.ROULETTE);
    }
}
