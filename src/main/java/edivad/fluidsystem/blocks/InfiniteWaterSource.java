package edivad.fluidsystem.blocks;

import edivad.fluidsystem.tile.TileEntityInfinityWaterSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class InfiniteWaterSource extends Block implements EntityBlock
{
    public InfiniteWaterSource()
    {
        super(Properties.of(Material.STONE).sound(SoundType.SNOW).strength(10.0F, 1200.0F));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityInfinityWaterSource(blockPos, blockState);
    }
}
