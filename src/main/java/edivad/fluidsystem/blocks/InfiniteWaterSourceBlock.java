package edivad.fluidsystem.blocks;

import edivad.fluidsystem.blockentity.InfinityWaterSourceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class InfiniteWaterSourceBlock extends Block implements EntityBlock {

    public InfiniteWaterSourceBlock() {
        super(Properties.of(Material.STONE).sound(SoundType.SNOW).strength(10.0F, 1200.0F));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfinityWaterSourceBlockEntity(pos, state);
    }
}
