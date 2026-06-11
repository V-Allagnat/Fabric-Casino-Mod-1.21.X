package fr.allagnat.casinomod.screen.custom;

import fr.allagnat.casinomod.block.ModBlockConstants;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record InventoryAddPayload(ItemStack itemStack, int amount) implements CustomPayload {
    public static final CustomPayload.Id<InventoryAddPayload> ID =
            new CustomPayload.Id<>(ModBlockConstants.INVENTORY_ADD_ID);
    public static final PacketCodec<RegistryByteBuf, InventoryAddPayload> CODEC =
            PacketCodec.tuple(
                    ItemStack.PACKET_CODEC, InventoryAddPayload::itemStack,
                    PacketCodecs.INTEGER, InventoryAddPayload::amount,
                    InventoryAddPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
