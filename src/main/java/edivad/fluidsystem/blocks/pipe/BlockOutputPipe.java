package edivad.fluidsystem.blocks.pipe;

import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import edivad.fluidsystem.tile.pipe.TileEntityBlockOutputPipe;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockOutputPipe extends BlockFilterable implements IFluidSystemConnectableBlock
{

    public BlockOutputPipe()
    {
        super(Properties.create(Material.IRON).sound(SoundType.STONE).hardnessAndResistance(5.0F));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBlockOutputPipe();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(Translations.OUTPUT_PIPE_TOOLTIP).mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public boolean canConnectTo(IWorld world, BlockPos myPos, Direction side)
    {
        BlockState state = world.getBlockState(myPos);
        return state.get(FACING).compareTo(side) == 0;
    }

    @Override
    public boolean isEndPoint(IWorld world, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean checkConnection(World world, BlockPos pos, Direction dir)
    {
        return canConnectTo(world, pos, dir);
    }
}
