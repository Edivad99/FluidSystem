package edivad.fluidsystem.tile.pipe;

import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.blocks.pipe.BlockInputPipe;
import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Routing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class TileEntityBlockInputPipe extends TileEntityBlockFilterablePipe {

    public TileEntityBlockInputPipe(BlockPos blockPos, BlockState blockState) {
        super(Registration.INPUT_PIPE_TILE.get(), blockPos, blockState);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileEntityBlockFilterablePipe blockTile) {
        if(!blockState.getValue(BlockInputPipe.POWERED))
            return;

        Direction inputFace = blockState.getValue(BlockInputPipe.FACING).getOpposite();
        BlockEntity tile = level.getBlockEntity(blockPos.relative(inputFace));
        if (tile == null)
            return;

        tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, inputFace.getOpposite()).ifPresent(input -> {
            for(int i = 0; i < input.getTanks(); i++) {
                FluidStack inputFluidStack = input.getFluidInTank(i);
                if(!inputFluidStack.isEmpty() && (blockTile.getFluidFilter().isSame(Fluids.EMPTY) || blockTile.getFluidFilter().isSame(inputFluidStack.getFluid()))) {
                    Fluid fluid = inputFluidStack.getFluid();
                    List<IFluidSystemEject> outputs = Routing.getBlockEject(level, blockPos, blockPos.relative(blockState.getValue(BlockInputPipe.FACING)));

                    int amountInput = inputFluidStack.getAmount();
                    for(IFluidSystemEject output : outputs) {
                        if(output.acceptFluid(fluid) && amountInput > 0) {
                            FluidStack test = input.drain(amountInput, IFluidHandler.FluidAction.SIMULATE);
                            int fluidAmountInserted = output.fill(test, IFluidHandler.FluidAction.SIMULATE);
                            if(fluidAmountInserted >= 0) {
                                FluidStack fluidExtracted = input.drain(fluidAmountInserted, IFluidHandler.FluidAction.EXECUTE);
                                int fluidAmountInsertedEffect = output.fill(fluidExtracted, IFluidHandler.FluidAction.EXECUTE);
                                amountInput = Math.max(0, amountInput - fluidAmountInsertedEffect);
                            }
                        }
                    }
                }
            }
        });
    }
}
