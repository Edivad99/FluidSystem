package edivad.fluidsystem.api;

import net.minecraft.fluid.Fluid;

public interface IFluidSystemFilterable
{

    void setFilteredFluid(Fluid fluid);

    Fluid getFluidFilter();
}
