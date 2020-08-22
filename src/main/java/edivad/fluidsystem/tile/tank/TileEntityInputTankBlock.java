package edivad.fluidsystem.tile.tank;

import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TileEntityInputTankBlock extends TileEntityBaseTankBlock implements IFluidSystemEject
{

    public TileEntityInputTankBlock()
    {
        super(Registration.INPUT_TANK_BLOCK_TILE.get());
    }

    @Override
    public boolean isMaster()
    {
        return false;
    }

    @Override
    public int blockCapacity()
    {
        return 0;
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action)
    {
        if(acceptFluid(resource.getFluid()))
        {
            TileEntityControllerTankBlock controller = (TileEntityControllerTankBlock) getMaster();
            if(controller != null)
            {
                AtomicInteger res = new AtomicInteger(0);
                controller.getFluidCap().ifPresent(h ->
                {
                    res.set(h.fill(resource, action));
                });
                return res.get();
            }
        }
        return 0;
    }

    @Override
    public boolean acceptFluid(Fluid fluidToInsert)
    {
        TileEntityControllerTankBlock controller = (TileEntityControllerTankBlock) getMaster();
        if(controller != null)
        {
            AtomicBoolean result = new AtomicBoolean(false);
            controller.getFluidCap().ifPresent(h ->
            {
                if(h.getFluidInTank(0).getFluid().isEquivalentTo(Fluids.EMPTY))
                    result.set(true);
                else
                    result.set(h.getFluidInTank(0).getFluid().isEquivalentTo(fluidToInsert));
            });
            return result.get();
        }
        return false;
    }
}
