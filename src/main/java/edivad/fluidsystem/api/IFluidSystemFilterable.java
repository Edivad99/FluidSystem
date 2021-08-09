package edivad.fluidsystem.api;

import net.minecraft.world.level.material.Fluid;

public interface IFluidSystemFilterable
{
    void setFilteredFluid(Fluid fluid);

    Fluid getFluidFilter();
}
