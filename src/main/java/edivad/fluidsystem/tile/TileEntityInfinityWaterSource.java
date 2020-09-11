package edivad.fluidsystem.tile;

import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.InfiniteTank;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityInfinityWaterSource extends TileEntity
{
    private final InfiniteTank tank = new InfiniteTank(Fluids.WATER);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    public TileEntityInfinityWaterSource()
    {
        super(Registration.INFINITE_WATER_SOURCE_TILE.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if(cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY))
            return fluidHandler.cast();
        return super.getCapability(cap, side);
    }
}
