package edivad.fluidsystem.tools;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class InfiniteTank implements IFluidHandler, IFluidTank
{

    private final Fluid fluid;

    public InfiniteTank(Fluid fluid)
    {
        this.fluid = fluid;
    }

    @Nonnull
    @Override
    public FluidStack getFluid()
    {
        return new FluidStack(fluid, getFluidAmount());
    }

    @Override
    public int getFluidAmount()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getCapacity()
    {
        return getFluidAmount();
    }

    @Override
    public boolean isFluidValid(FluidStack stack)
    {
        return stack.getFluid().isEquivalentTo(fluid);
    }

    @Override
    public int getTanks()
    {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return getFluidAmount();
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)
    {
        if(resource.isEmpty() || !resource.isFluidEqual(resource))
        {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        return new FluidStack(fluid, maxDrain);
    }
}
