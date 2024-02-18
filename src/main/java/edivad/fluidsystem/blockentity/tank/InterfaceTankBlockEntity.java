package edivad.fluidsystem.blockentity.tank;

import edivad.fluidsystem.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class InterfaceTankBlockEntity extends BaseTankBlockEntity {

  public InterfaceTankBlockEntity(BlockPos pos, BlockState state) {
    super(Registration.INTERFACE_TANK_BLOCK_ENTITY.get(), pos, state);
  }

  @Override
  public boolean isMaster() {
    return false;
  }

  @Override
  public int blockCapacity() {
    return 0;
  }

  public IFluidHandler getFluidCap(Direction side) {
    var controller = (ControllerTankBlockEntity) getMaster();
    if (controller != null) {
      return controller.getFluidCap();
    }
    return null;
  }
}
