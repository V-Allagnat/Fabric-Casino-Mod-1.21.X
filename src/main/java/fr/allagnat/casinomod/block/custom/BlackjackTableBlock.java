package fr.allagnat.casinomod.block.custom;

import com.mojang.serialization.MapCodec;
import fr.allagnat.casinomod.block.entity.custom.BlackjackTableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlackjackTableBlock extends BlockWithEntity implements BlockEntityProvider {

    private static final VoxelShape SHAPE = VoxelShapes.union(
            VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 0.0625, 0.75),
            VoxelShapes.cuboid(0.4375, 0.0625, 0.4375, 0.5625, 0.9375, 0.5625),
            VoxelShapes.cuboid(0, 0.9375, 0, 1, 1, 1),
            VoxelShapes.cuboid(0.3125, 0.8125, 0.3125, 0.6875, 0.9375, 0.6875)
    );

    public static final MapCodec<BlackjackTableBlock> CODEC = BlackjackTableBlock.createCodec(BlackjackTableBlock::new);

    public BlackjackTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlackjackTableBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BlackjackTableBlockEntity) {
                ItemScatterer.spawn(world, pos, ((BlackjackTableBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
                                             PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof BlackjackTableBlockEntity blackjackTableBlockEntity) {
            if (!player.isSneaking() && !world.isClient()) {
                player.openHandledScreen(blackjackTableBlockEntity);

            } else if (player.isSneaking() && stack.isEmpty()) {
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 2f, 1f);
                ItemStack stackInTable = blackjackTableBlockEntity.getStack(0);
                player.setStackInHand(Hand.MAIN_HAND, stackInTable);
                blackjackTableBlockEntity.clear();

                blackjackTableBlockEntity.markDirty();
                world.updateListeners(pos, state, state, 0);
            }
        }
        return ItemActionResult.SUCCESS;
    }
}
