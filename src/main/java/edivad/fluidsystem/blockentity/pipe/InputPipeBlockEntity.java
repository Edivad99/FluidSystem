package edivad.fluidsystem.blockentity.pipe;

import java.util.List;
import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.blocks.pipe.InputPipeBlock;
import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Routing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class InputPipeBlockEntity extends FilterablePipeBlockEntity {

  public InputPipeBlockEntity(BlockPos pos, BlockState state) {
    super(Registration.INPUT_PIPE_TILE.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos pos, BlockState state,
      FilterablePipeBlockEntity blockTile) {
    if (!state.getValue(InputPipeBlock.POWERED)) {
      return;
    }

    Direction inputFace = state.getValue(InputPipeBlock.FACING).getOpposite();
    BlockEntity blockentity = level.getBlockEntity(pos.relative(inputFace));
    if (blockentity == null) {
      return;
    }

    blockentity.getCapability(ForgeCapabilities.FLUID_HANDLER, inputFace.getOpposite())
        .ifPresent(input -> {
          for (int i = 0; i < input.getTanks(); i++) {
            FluidStack inputFluidStack = input.getFluidInTank(i);
            if (!inputFluidStack.isEmpty() && (blockTile.getFluidFilter().isSame(Fluids.EMPTY)
                || blockTile.getFluidFilter().isSame(inputFluidStack.getFluid()))) {
              Fluid fluid = inputFluidStack.getFluid();
              List<IFluidSystemEject> outputs = Routing.getBlockEject(level, pos,
                  pos.relative(state.getValue(InputPipeBlock.FACING)));

              int amountInput = inputFluidStack.getAmount();
              for (IFluidSystemEject output : outputs) {
                if (output.acceptFluid(fluid) && amountInput > 0) {
                  FluidStack test = input.drain(amountInput, IFluidHandler.FluidAction.SIMULATE);
                  int fluidAmountInserted = output.fill(test, IFluidHandler.FluidAction.SIMULATE);
                  if (fluidAmountInserted >= 0) {
                    FluidStack fluidExtracted = input.drain(fluidAmountInserted,
                        IFluidHandler.FluidAction.EXECUTE);
                    int fluidAmountInsertedEffect = output.fill(fluidExtracted,
                        IFluidHandler.FluidAction.EXECUTE);
                    amountInput = Math.max(0, amountInput - fluidAmountInsertedEffect);
                  }
                }
              }
            }
          }
        });
  }
}
