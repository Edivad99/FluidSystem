package edivad.fluidsystem.blocks.tank;

import edivad.fluidsystem.tile.tank.TileEntityBaseTankBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaseBlock extends Block
{
    public BaseBlock()
    {
        super(Properties.create(Material.IRON).sound(SoundType.STONE).hardnessAndResistance(5.0F));
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if(!worldIn.isRemote)
        {
            TileEntity tile = worldIn.getTileEntity(pos);

            if(tile instanceof TileEntityBaseTankBlock && placer instanceof PlayerEntity)
            {
                ((TileEntityBaseTankBlock) tile).onBlockPlacedBy((PlayerEntity)placer, worldIn, pos);
            }
        }
    }
}
