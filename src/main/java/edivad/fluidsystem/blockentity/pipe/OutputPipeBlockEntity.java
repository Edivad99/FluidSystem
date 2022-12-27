package edivad.fluidsystem.blockentity.pipe;

import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.blocks.pipe.OutputPipeBlock;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class OutputPipeBlockEntity extends FilterablePipeBlockEntity implements IFluidSystemEject {

    public OutputPipeBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.OUTPUT_PIPE_TILE.get(), pos, state);
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if(acceptFluid(resource.getFluid())) {
            Direction outputFace = this.getBlockState().getValue(OutputPipeBlock.FACING);
            BlockEntity blockentity = level.getBlockEntity(getBlockPos().relative(outputFace));
            if(blockentity != null) {
                AtomicInteger res = new AtomicInteger(0);
                blockentity.getCapability(ForgeCapabilities.FLUID_HANDLER, outputFace.getOpposite()).ifPresent(h -> {
                    int r = h.fill(resource, action);
                    res.set(r);
                });
                return res.get();
            }
        }
        return 0;
    }

    @Override
    public boolean acceptFluid(Fluid fluidToInsert) {
        if(getFluidFilter().isSame(Fluids.EMPTY) || getFluidFilter().isSame(fluidToInsert)) {
            Direction outputFace = this.getBlockState().getValue(OutputPipeBlock.FACING);
            BlockEntity blockentity = level.getBlockEntity(getBlockPos().relative(outputFace));
            if(blockentity != null) {
                AtomicBoolean result = new AtomicBoolean(false);
                blockentity.getCapability(ForgeCapabilities.FLUID_HANDLER, outputFace.getOpposite()).ifPresent(h -> {
                    Fluid fluidInsideTank = h.getFluidInTank(0).getFluid();
                    if(fluidInsideTank.isSame(Fluids.EMPTY))
                        result.set(true);
                    else
                        result.set(fluidInsideTank.isSame(fluidToInsert));
                });
                return result.get();
            }
        }
        return false;
    }
}
