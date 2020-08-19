package edivad.fluidsystem.tile.tank;

import edivad.fluidsystem.setup.Registration;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileEntityInterfaceTankBlock extends TileEntityBaseTankBlock
{
    public TileEntityInterfaceTankBlock()
    {
        super(Registration.INTERFACE_TANK_BLOCK_TILE.get());
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
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if(cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY))
        {
            TileEntityControllerTankBlock controller = (TileEntityControllerTankBlock)getMaster();
            if(controller != null)
                return controller.getFluidCap().cast();
        }
        return super.getCapability(cap, side);
    }
}
