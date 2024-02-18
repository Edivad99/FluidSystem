package edivad.fluidsystem.blockentity.pipe;

import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.blocks.pipe.OutputPipeBlock;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class OutputPipeBlockEntity extends FilterablePipeBlockEntity implements IFluidSystemEject {

  public OutputPipeBlockEntity(BlockPos pos, BlockState state) {
    super(Registration.OUTPUT_PIPE_BLOCK_ENTITY.get(), pos, state);
  }

  @Override
  public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
    if (acceptFluid(resource.getFluid())) {
      var outputFace = this.getBlockState().getValue(OutputPipeBlock.FACING);
      var outputFluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK,
          getBlockPos().relative(outputFace), outputFace.getOpposite());
      if (outputFluidHandler != null) {
        return outputFluidHandler.fill(resource, action);
      }
    }
    return 0;
  }

  @Override
  public boolean acceptFluid(Fluid fluidToInsert) {
    if (getFluidFilter().isSame(Fluids.EMPTY) || getFluidFilter().isSame(fluidToInsert)) {
      var outputFace = this.getBlockState().getValue(OutputPipeBlock.FACING);
      var outputFluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK,
          getBlockPos().relative(outputFace), outputFace.getOpposite());
      if (outputFluidHandler != null) {
        var fluidStack = outputFluidHandler.getFluidInTank(0);
        return fluidStack.isEmpty() || fluidStack.is(fluidToInsert);
      }
    }
    return false;
  }
}
