package edivad.fluidsystem.api;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Tile entities that implement this interface will be able to receive fluid from the pressure system
 */
public interface IFluidSystemEject
{

    /**
     * Called when fluid is eject from the FluidSystem's pipes into this TE
     *
     * @param resource Fluid to eject
     * @param action   If SIMULATE, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated) filled
     */
    int fill(FluidStack resource, IFluidHandler.FluidAction action);

    /**
     * @param fluidToInsert Fluid to check if it can be accepted
     * @return true if the Fluid is accepted
     */
    boolean acceptFluid(Fluid fluidToInsert);
}
