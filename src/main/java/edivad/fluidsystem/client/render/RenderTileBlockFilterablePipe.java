package edivad.fluidsystem.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import edivad.fluidsystem.blocks.pipe.BlockOutputPipe;
import edivad.fluidsystem.tile.pipe.TileEntityBlockFilterablePipe;
import edivad.fluidsystem.tools.utils.FluidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class RenderTileBlockFilterablePipe extends TileEntityRenderer<TileEntityBlockFilterablePipe>
{

    public RenderTileBlockFilterablePipe(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityBlockFilterablePipe tile, float partialTicks, MatrixStack mStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        if(tile == null || tile.isRemoved() || tile.getClientFluid().isEquivalentTo(Fluids.EMPTY))
            return;

        FluidStack fluid = new FluidStack(tile.getClientFluid(), 1000);
        TextureAtlasSprite sprite = FluidUtils.getFluidTexture(fluid);
        if(sprite == null)
            return;

        mStack.push();
        Matrix4f matrix4f = mStack.getLast().getMatrix();
        IVertexBuilder renderer = bufferIn.getBuffer(RenderType.getText(sprite.getAtlasTexture().getTextureLocation()));

        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();
        float u2 = sprite.getMaxU();
        float v2 = sprite.getMaxV();

        int color = FluidUtils.getLiquidColorWithBiome(fluid, tile);

        float r = FluidUtils.getRed(color);
        float g = FluidUtils.getGreen(color);
        float b = FluidUtils.getBlue(color);
        float a = FluidUtils.getAlpha(color);
        int light = 15728880;

        BlockState state = tile.getBlockState();
        Direction dir = state.get(BlockOutputPipe.FACING);

        if(dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH))
        {
            float offsetSide = dir.equals(Direction.NORTH) ? 0 : 0.19f;
            //WEST
            renderer.pos(matrix4f, 1.001f, 0.66f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
            renderer.pos(matrix4f, 1.001f, 0.34f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 1.001f, 0.34f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 1.001f, 0.66f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

            //EAST
            renderer.pos(matrix4f, -0.001f, 0.34f, 0.435f - offsetSide).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
            renderer.pos(matrix4f, -0.001f, 0.34f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, -0.001f, 0.66f, 0.752f - offsetSide).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, -0.001f, 0.66f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

            //UP
            renderer.pos(matrix4f, 0.34f, 1.001f, 0.435f - offsetSide).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.34f, 1.001f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.66f, 1.001f, 0.752f - offsetSide).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.66f, 1.001f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

            //DOWN
            renderer.pos(matrix4f, 0.66f, -0.001f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.34f, -0.001f, 0.752f - offsetSide).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.34f, -0.001f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.66f, -0.001f, 0.435f - offsetSide).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
        }
        else if(dir.equals(Direction.EAST) || dir.equals(Direction.WEST))
        {
            float offsetSide = dir.equals(Direction.EAST) ? 0 : 0.19f;

            //NORTH
            renderer.pos(matrix4f, 0.245f + offsetSide, 0.657f, 1.001f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.245f + offsetSide, 0.339f, 1.001f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.565f + offsetSide, 0.339f, 1.001f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.565f + offsetSide, 0.657f, 1.001f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

            //SOUTH
            renderer.pos(matrix4f, 0.245f + offsetSide, 0.339f, -0.001f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.245f + offsetSide, 0.657f, -0.001f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.565f + offsetSide, 0.657f, -0.001f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.565f + offsetSide, 0.339f, -0.001f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

            //UP
            renderer.pos(matrix4f, 0.245f + offsetSide, 1.001f, 0.339f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.245f + offsetSide, 1.001f, 0.657f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.565f + offsetSide, 1.001f, 0.657f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.565f + offsetSide, 1.001f, 0.339f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

            //DOWN
            renderer.pos(matrix4f, 0.565f + offsetSide, -0.001f, 0.657f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.245f + offsetSide, -0.001f, 0.657f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.245f + offsetSide, -0.001f, 0.339f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
            renderer.pos(matrix4f, 0.565f + offsetSide, -0.001f, 0.339f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
        }
        mStack.pop();
    }
}
