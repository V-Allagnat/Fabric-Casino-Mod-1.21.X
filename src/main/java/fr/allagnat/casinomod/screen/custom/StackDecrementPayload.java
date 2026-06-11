package fr.allagnat.casinomod.screen.custom;

import fr.allagnat.casinomod.block.ModBlockConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record StackDecrementPayload(BlockPos blockPos, int amount) implements CustomPayload {
    public static final CustomPayload.Id<StackDecrementPayload> ID =
            new CustomPayload.Id<>(ModBlockConstants.STACK_DECREMENT_ID);
    public static final PacketCodec<RegistryByteBuf, StackDecrementPayload> CODEC =
            PacketCodec.tuple(
                    BlockPos.PACKET_CODEC, StackDecrementPayload::blockPos,
                    PacketCodecs.INTEGER, StackDecrementPayload::amount,
                    StackDecrementPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
