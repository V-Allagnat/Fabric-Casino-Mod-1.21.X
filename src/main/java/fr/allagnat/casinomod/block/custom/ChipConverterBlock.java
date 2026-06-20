package fr.allagnat.casinomod.block.custom;

import com.mojang.serialization.MapCodec;
import fr.allagnat.casinomod.block.entity.custom.ChipConverterBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChipConverterBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<ChipConverterBlock> CODEC = ChipConverterBlock.createCodec(ChipConverterBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public ChipConverterBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChipConverterBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ChipConverterBlockEntity) {
                ItemScatterer.spawn(world, pos, ((ChipConverterBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof ChipConverterBlockEntity chipConverterBlockEntity && !world.isClient()) {
            if (chipConverterBlockEntity.getCurrentUserUUID() != null) {
                // another player is currently using the interface
                return ItemActionResult.SUCCESS;
            }
            // lock screen so no other players can use it
            UUID playerUUID = player.getUuid();
            if (playerUUID == null) {
                return ItemActionResult.SUCCESS;
            }
            chipConverterBlockEntity.setCurrentUserUUID(playerUUID);

            player.openHandledScreen(chipConverterBlockEntity);
        }
        return ItemActionResult.SUCCESS;
    }
}
