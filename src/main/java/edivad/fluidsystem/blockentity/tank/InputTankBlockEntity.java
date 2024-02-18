package edivad.fluidsystem.blockentity.tank;

import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class InputTankBlockEntity extends BaseTankBlockEntity implements IFluidSystemEject {

  public InputTankBlockEntity(BlockPos pos, BlockState state) {
    super(Registration.INPUT_TANK_BLOCK_ENTITY.get(), pos, state);
  }

  @Override
  public boolean isMaster() {
    return false;
  }

  @Override
  public int blockCapacity() {
    return 0;
  }

  @Override
  public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
    if (acceptFluid(resource.getFluid())) {
      var controller = (ControllerTankBlockEntity) getMaster();
      if (controller != null) {
        var fluidCap = controller.getFluidCap();
        if (fluidCap != null) {
          return fluidCap.fill(resource, action);
        }
      }
    }
    return 0;
  }

  @Override
  public boolean acceptFluid(Fluid fluidToInsert) {
    var controller = (ControllerTankBlockEntity) getMaster();
    if (controller != null) {
      var fluidCap = controller.getFluidCap();
      if (fluidCap != null) {
        var fluidStack = fluidCap.getFluidInTank(0);
        return fluidStack.isEmpty() || fluidStack.is(fluidToInsert);
      }
    }
    return false;
  }
}
