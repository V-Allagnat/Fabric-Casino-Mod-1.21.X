package fr.allagnat.casinomod.block.entity.renderer;

import fr.allagnat.casinomod.block.entity.custom.RouletteBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class RouletteBlockEntityRenderer implements BlockEntityRenderer<RouletteBlockEntity> {
    public RouletteBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        // FIXME empty
    }

    @Override
    public void render(RouletteBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // FIXME empty
    }
}
