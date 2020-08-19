package edivad.fluidsystem.blocks.tank;

import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ControllerTankBlock extends BaseBlock
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
        return new TileEntityControllerTankBlock();
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(worldIn.isRemote)
            return ActionResultType.SUCCESS;

        TileEntity tile = worldIn.getTileEntity(pos);

        if(tile instanceof TileEntityControllerTankBlock)
        {
            return ((TileEntityControllerTankBlock) tile).activate(player, worldIn, pos, handIn);
        }
        return ActionResultType.FAIL;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(Translations.TANK_BLOCK_CONTROLLER_TOOLTIP).mergeStyle(TextFormatting.GRAY));
    }
}
