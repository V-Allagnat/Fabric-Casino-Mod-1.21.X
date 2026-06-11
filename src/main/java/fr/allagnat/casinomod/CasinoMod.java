package fr.allagnat.casinomod;

import fr.allagnat.casinomod.block.ModBlocks;
import fr.allagnat.casinomod.block.entity.ModBlockEntities;
import fr.allagnat.casinomod.block.entity.custom.BlackjackTableBlockEntity;
import fr.allagnat.casinomod.block.entity.custom.ChipConverterBlockEntity;
import fr.allagnat.casinomod.item.ModItemGroups;
import fr.allagnat.casinomod.item.ModItems;
import fr.allagnat.casinomod.screen.ModScreenHandlers;
import fr.allagnat.casinomod.screen.custom.InventoryAddPayload;
import fr.allagnat.casinomod.screen.custom.StackDecrementPayload;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CasinoMod implements ModInitializer {
	public static final String MOD_ID = "casinomod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();

		ModScreenHandlers.registerScreenHandlers();

		PayloadTypeRegistry.playC2S().register(StackDecrementPayload.ID, StackDecrementPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(InventoryAddPayload.ID, InventoryAddPayload.CODEC);

		// Server side stack decrementing
		// Can also achieve this by scheduling a task on the server thread using .execute() like so:
//		context.server().execute(() -> {
//			blackjackTable.getStack(0).decrement(payload.amount());
//		});
		ServerPlayNetworking.registerGlobalReceiver(StackDecrementPayload.ID, (payload, context) -> {
			if (context.player().getWorld().getBlockEntity(payload.blockPos()) instanceof BlackjackTableBlockEntity blackjackTable) {
				blackjackTable.getStack(0).decrement(payload.amount());
			} else if (context.player().getWorld().getBlockEntity(payload.blockPos()) instanceof ChipConverterBlockEntity chipConverter) {
				chipConverter.getStack(0).decrement(payload.amount());
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(InventoryAddPayload.ID, (payload, context) -> {
			ItemStack s = payload.itemStack();
			s.setCount(payload.amount());
			if (s.getCount() != 0) {
				int nonFullSlot = context.player().getInventory().getOccupiedSlotWithRoomForStack(s);
				while (nonFullSlot != -1) {
					int remainingSpace = 64 - context.player().getInventory().getStack(nonFullSlot).getCount();
					int toMove = Math.min(s.getCount(), remainingSpace);
					s.decrement(toMove);
					context.player().getInventory().getStack(nonFullSlot).increment(toMove);
					nonFullSlot = context.player().getInventory().getOccupiedSlotWithRoomForStack(s);
				}
				int emptySlot = context.player().getInventory().getEmptySlot();
				while (emptySlot != -1 && s.getCount() > 0) {
					int toInsert = Math.min(64, s.getCount());
					ItemStack stackToInsert = new ItemStack(s.getItem(), toInsert);
					context.player().getInventory().insertStack(nonFullSlot, stackToInsert);
					s.decrement(toInsert);
					emptySlot = context.player().getInventory().getEmptySlot();
				}
				if (s.getCount() == 0) {
					// Everything has been inserted already, no need to scatter an empty ItemStack
					return;
				}
				ItemScatterer.spawn(context.player().getWorld(), context.player().getX(), context.player().getY(), context.player().getZ(), s);
			}
		});
	}
}