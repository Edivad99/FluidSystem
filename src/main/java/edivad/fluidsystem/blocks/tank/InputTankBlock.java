package edivad.fluidsystem.blocks.tank;

import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import edivad.fluidsystem.tile.tank.TileEntityInputTankBlock;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.block.BlockState;
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

public class InputTankBlock extends BaseBlockRotable implements IFluidSystemConnectableBlock
{

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityInputTankBlock();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(Translations.TANK_BLOCK_INPUT_TOOLTIP).mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public boolean canConnectTo(IWorld world, BlockPos myPos, Direction side)
    {
        BlockState state = world.getBlockState(myPos);
        return state.get(FACING).compareTo(side.getOpposite()) == 0;
    }

    @Override
    public boolean isEndPoint(IWorld world, BlockPos myPos)
    {
        return true;
    }

    @Override
    public boolean checkConnection(World world, BlockPos pos, Direction dir)
    {
        return canConnectTo(world, pos, dir);
    }
}
