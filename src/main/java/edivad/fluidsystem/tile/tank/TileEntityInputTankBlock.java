package edivad.fluidsystem.tile.tank;

import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TileEntityInputTankBlock extends TileEntityBaseTankBlock implements IFluidSystemEject
{
    public TileEntityInputTankBlock(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.INPUT_TANK_BLOCK_TILE.get(), blockPos, blockState);
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
                if(h.getFluidInTank(0).getFluid().isSame(Fluids.EMPTY))
                    result.set(true);
                else
                    result.set(h.getFluidInTank(0).getFluid().isSame(fluidToInsert));
            });
            return result.get();
        }
        return false;
    }
}
