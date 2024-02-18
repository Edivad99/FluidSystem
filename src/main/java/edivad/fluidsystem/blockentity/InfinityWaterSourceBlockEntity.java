package edivad.fluidsystem.blockentity;

import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.InfiniteTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class InfinityWaterSourceBlockEntity extends BlockEntity {

  private final InfiniteTank tank = new InfiniteTank(Fluids.WATER);

  public InfinityWaterSourceBlockEntity(BlockPos pos, BlockState state) {
    super(Registration.INFINITE_WATER_SOURCE_BLOCK_ENTITY.get(), pos, state);
  }

  public IFluidHandler getFluidCap(Direction side) {
    return this.tank;
  }
}
