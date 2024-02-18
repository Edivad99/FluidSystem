package edivad.fluidsystem.blockentity.pipe;

import edivad.fluidsystem.blocks.pipe.InputPipeBlock;
import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Routing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class InputPipeBlockEntity extends FilterablePipeBlockEntity {

  public InputPipeBlockEntity(BlockPos pos, BlockState state) {
    super(Registration.INPUT_PIPE_BLOCK_ENTITY.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos pos, BlockState state,
      FilterablePipeBlockEntity blockTile) {
    if (!state.getValue(InputPipeBlock.POWERED)) {
      return;
    }

    var inputFace = state.getValue(InputPipeBlock.FACING).getOpposite();
    var inputFluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK,
        pos.relative(inputFace), inputFace.getOpposite());
    if (inputFluidHandler != null) {
      for (int i = 0; i < inputFluidHandler.getTanks(); i++) {
        var inputFluidStack = inputFluidHandler.getFluidInTank(i);
        if (!inputFluidStack.isEmpty() && (blockTile.getFluidFilter().isSame(Fluids.EMPTY)
            || blockTile.getFluidFilter().isSame(inputFluidStack.getFluid()))) {
          var fluid = inputFluidStack.getFluid();
          var outputs = Routing.getBlockEject(level, pos, pos.relative(state.getValue(InputPipeBlock.FACING)));

          int amountInput = inputFluidStack.getAmount();
          for (var output : outputs) {
            if (output.acceptFluid(fluid) && amountInput > 0) {
              var test = inputFluidHandler.drain(amountInput, IFluidHandler.FluidAction.SIMULATE);
              int fluidAmountInserted = output.fill(test, IFluidHandler.FluidAction.SIMULATE);
              if (fluidAmountInserted >= 0) {
                var fluidExtracted = inputFluidHandler.drain(fluidAmountInserted, IFluidHandler.FluidAction.EXECUTE);
                int fluidAmountInsertedEffect = output.fill(fluidExtracted,
                    IFluidHandler.FluidAction.EXECUTE);
                amountInput = Math.max(0, amountInput - fluidAmountInsertedEffect);
              }
            }
          }
        }
      }
    }
  }
}
