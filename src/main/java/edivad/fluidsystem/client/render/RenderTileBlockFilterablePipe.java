package edivad.fluidsystem.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import edivad.fluidsystem.blocks.pipe.BlockOutputPipe;
import edivad.fluidsystem.tile.pipe.TileEntityBlockFilterablePipe;
import edivad.fluidsystem.tools.utils.FluidUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class RenderTileBlockFilterablePipe implements BlockEntityRenderer<TileEntityBlockFilterablePipe> {

    public RenderTileBlockFilterablePipe(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TileEntityBlockFilterablePipe tile, float partialTicks, PoseStack mStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(tile.isRemoved() || tile.getFluidFilter().isSame(Fluids.EMPTY))
            return;

        FluidStack fluid = new FluidStack(tile.getFluidFilter(), 1000);
        TextureAtlasSprite sprite = FluidUtils.getFluidTexture(fluid);
        if(sprite == null)
            return;

        mStack.pushPose();
        Matrix4f matrix4f = mStack.last().pose();
        VertexConsumer renderer = bufferIn.getBuffer(RenderType.text(sprite.atlas().location()));

        float u1 = sprite.getU0();
        float v1 = sprite.getV0();
        float u2 = sprite.getU1();
        float v2 = sprite.getV1();

        int color = FluidUtils.getLiquidColorWithBiome(fluid, tile);

        float r = FluidUtils.getRed(color);
        float g = FluidUtils.getGreen(color);
        float b = FluidUtils.getBlue(color);
        float a = FluidUtils.getAlpha(color);
        int light = 15728880;

        BlockState state = tile.getBlockState();
        Direction dir = state.getValue(BlockOutputPipe.FACING);

        if(dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH)) {
            float offsetSide = dir.equals(Direction.NORTH) ? 0 : 0.19f;
            //WEST
            renderer.vertex(matrix4f, 1.001f, 0.66f, 0.752f - offsetSide).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
            renderer.vertex(matrix4f, 1.001f, 0.34f, 0.752f - offsetSide).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 1.001f, 0.34f, 0.435f - offsetSide).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 1.001f, 0.66f, 0.435f - offsetSide).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();

            //EAST
            renderer.vertex(matrix4f, -0.001f, 0.34f, 0.435f - offsetSide).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
            renderer.vertex(matrix4f, -0.001f, 0.34f, 0.752f - offsetSide).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, -0.001f, 0.66f, 0.752f - offsetSide).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, -0.001f, 0.66f, 0.435f - offsetSide).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();

            //UP
            renderer.vertex(matrix4f, 0.34f, 1.001f, 0.435f - offsetSide).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.34f, 1.001f, 0.752f - offsetSide).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.66f, 1.001f, 0.752f - offsetSide).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.66f, 1.001f, 0.435f - offsetSide).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();

            //DOWN
            renderer.vertex(matrix4f, 0.66f, -0.001f, 0.752f - offsetSide).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.34f, -0.001f, 0.752f - offsetSide).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.34f, -0.001f, 0.435f - offsetSide).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.66f, -0.001f, 0.435f - offsetSide).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();
        }
        else if(dir.equals(Direction.EAST) || dir.equals(Direction.WEST)) {
            float offsetSide = dir.equals(Direction.EAST) ? 0 : 0.19f;

            //NORTH
            renderer.vertex(matrix4f, 0.245f + offsetSide, 0.657f, 1.001f).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.245f + offsetSide, 0.339f, 1.001f).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.565f + offsetSide, 0.339f, 1.001f).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.565f + offsetSide, 0.657f, 1.001f).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();

            //SOUTH
            renderer.vertex(matrix4f, 0.245f + offsetSide, 0.339f, -0.001f).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.245f + offsetSide, 0.657f, -0.001f).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.565f + offsetSide, 0.657f, -0.001f).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.565f + offsetSide, 0.339f, -0.001f).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();

            //UP
            renderer.vertex(matrix4f, 0.245f + offsetSide, 1.001f, 0.339f).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.245f + offsetSide, 1.001f, 0.657f).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.565f + offsetSide, 1.001f, 0.657f).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.565f + offsetSide, 1.001f, 0.339f).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();

            //DOWN
            renderer.vertex(matrix4f, 0.565f + offsetSide, -0.001f, 0.657f).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.245f + offsetSide, -0.001f, 0.657f).color(r, g, b, a).uv(u1, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.245f + offsetSide, -0.001f, 0.339f).color(r, g, b, a).uv(u2, v2).uv2(light).endVertex();
            renderer.vertex(matrix4f, 0.565f + offsetSide, -0.001f, 0.339f).color(r, g, b, a).uv(u2, v1).uv2(light).endVertex();
        }
        mStack.popPose();
    }
}
