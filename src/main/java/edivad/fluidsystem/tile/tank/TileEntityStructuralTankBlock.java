package edivad.fluidsystem.tile.tank;

import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityStructuralTankBlock extends TileEntityBaseTankBlock {

    public TileEntityStructuralTankBlock(BlockPos blockPos, BlockState blockState) {
        super(Registration.STRUCTURAL_TANK_BLOCK_TILE.get(), blockPos, blockState);
    }

    @Override
    public boolean isMaster() {
        return false;
    }

    @Override
    public int blockCapacity() {
        return Config.BLOCK_CAPACITY.get();
    }
}
