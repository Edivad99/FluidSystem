package edivad.fluidsystem.tile.pipe;

import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.blocks.pipe.BlockOutputPipe;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TileEntityBlockOutputPipe extends TileEntityBlockFilterablePipe implements IFluidSystemEject
{
    public TileEntityBlockOutputPipe()
    {
        super(Registration.OUTPUT_PIPE_TILE.get());
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action)
    {
        if(acceptFluid(resource.getFluid()))
        {
            Direction outputFace = this.getBlockState().get(BlockOutputPipe.FACING);
            TileEntity tile = world.getTileEntity(getPos().offset(outputFace));
            if(tile != null)
            {
                AtomicInteger res = new AtomicInteger(0);
                tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFace.getOpposite()).ifPresent(h ->
                {
                    int r = h.fill(resource, action);
                    res.set(r);
                });
                return res.get();
            }
        }
        return 0;
    }

    @Override
    public boolean acceptFluid(Fluid fluidToInsert)
    {
        if(getFluidFilter().isEquivalentTo(Fluids.EMPTY) || getFluidFilter().isEquivalentTo(fluidToInsert))
        {
            Direction outputFace = this.getBlockState().get(BlockOutputPipe.FACING);
            TileEntity tile = world.getTileEntity(getPos().offset(outputFace));
            if(tile != null)
            {
                AtomicBoolean result = new AtomicBoolean(false);
                tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, outputFace.getOpposite()).ifPresent(h ->
                {
                    Fluid fluidInsideTank = h.getFluidInTank(0).getFluid();
                    if(fluidInsideTank.isEquivalentTo(Fluids.EMPTY))
                        result.set(true);
                    else
                        result.set(fluidInsideTank.isEquivalentTo(fluidToInsert));
                });
                return result.get();
            }
        }
        return false;
    }
}
