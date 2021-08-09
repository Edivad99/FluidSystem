package edivad.fluidsystem.tile.pipe;

import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.blocks.pipe.BlockOutputPipe;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TileEntityBlockOutputPipe extends TileEntityBlockFilterablePipe implements IFluidSystemEject {

    public TileEntityBlockOutputPipe(BlockPos blockPos, BlockState blockState) {
        super(Registration.OUTPUT_PIPE_TILE.get(), blockPos, blockState);
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if(acceptFluid(resource.getFluid())) {
            Direction outputFace = this.getBlockState().getValue(BlockOutputPipe.FACING);
            BlockEntity tile = level.getBlockEntity(getBlockPos().relative(outputFace));
            if(tile != null) {
                AtomicInteger res = new AtomicInteger(0);
                tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFace.getOpposite()).ifPresent(h -> {
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
            Direction outputFace = this.getBlockState().getValue(BlockOutputPipe.FACING);
            BlockEntity tile = level.getBlockEntity(getBlockPos().relative(outputFace));
            if(tile != null) {
                AtomicBoolean result = new AtomicBoolean(false);
                tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFace.getOpposite()).ifPresent(h -> {
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
