package edivad.fluidsystem.blocks.pipe;

import edivad.fluidsystem.blocks.BlockRotable;
import edivad.fluidsystem.tile.pipe.TileEntityBlockFilterablePipe;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class BlockFilterable extends BlockRotable
{
    public BlockFilterable(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(worldIn.isRemote)
            return ActionResultType.SUCCESS;

        ItemStack itemOnHand = player.getHeldItem(handIn);
        if(!itemOnHand.isEmpty() && itemOnHand.getItem() instanceof BucketItem)
        {
            BucketItem bucket = ((BucketItem) itemOnHand.getItem());
            TileEntity tile = worldIn.getTileEntity(pos);
            if(tile instanceof TileEntityBlockFilterablePipe)
            {
                if(bucket.getFluid().isEquivalentTo(Fluids.EMPTY))
                {
                    if(!((TileEntityBlockFilterablePipe) tile).getFluidFilter().isEquivalentTo(Fluids.EMPTY))
                        player.sendStatusMessage(new TranslationTextComponent(Translations.FLUID_FILTERED_REMOVE), false);
                }
                else
                {
                    String fluidName = bucket.getFluid().getAttributes().getDisplayName(null).getString();
                    player.sendStatusMessage(new TranslationTextComponent(Translations.FLUID_FILTERED_SET, fluidName).mergeStyle(TextFormatting.GREEN), false);
                }
                ((TileEntityBlockFilterablePipe) tile).setFilteredFluid(bucket.getFluid());
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }
}
