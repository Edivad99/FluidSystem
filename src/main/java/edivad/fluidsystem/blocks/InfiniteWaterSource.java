package edivad.fluidsystem.blocks;

import edivad.fluidsystem.tile.TileEntityInfinityWaterSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class InfiniteWaterSource extends Block
{
    public InfiniteWaterSource()
    {
        super(Properties.create(Material.ROCK).sound(SoundType.SNOW).hardnessAndResistance(10.0F, 1200.0F));
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
        return new TileEntityInfinityWaterSource();
    }
}
