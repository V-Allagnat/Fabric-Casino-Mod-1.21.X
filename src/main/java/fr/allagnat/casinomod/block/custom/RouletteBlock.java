package fr.allagnat.casinomod.block.custom;

import com.mojang.serialization.MapCodec;
import fr.allagnat.casinomod.block.entity.custom.RouletteBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RouletteBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<RouletteBlock> CODEC = RouletteBlock.createCodec(RouletteBlock::new);

    public RouletteBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RouletteBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RouletteBlockEntity) {
                ItemScatterer.spawn(world, pos, ((RouletteBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof RouletteBlockEntity rouletteBlockEntity && !world.isClient()) {
            UUID currentUser = rouletteBlockEntity.getCurrentUserUUID();
            if (currentUser != null && !currentUser.equals(player.getUuid())) {
                // another player is currently using the interface
                return ItemActionResult.SUCCESS;
            }
            // lock screen so no other players can use it
            UUID playerUUID = player.getUuid();
            if (playerUUID == null) {
                return ItemActionResult.SUCCESS;
            }
            rouletteBlockEntity.setCurrentUserUUID(playerUUID);

            player.openHandledScreen(rouletteBlockEntity);
        }
        return ItemActionResult.SUCCESS;
    }
}
