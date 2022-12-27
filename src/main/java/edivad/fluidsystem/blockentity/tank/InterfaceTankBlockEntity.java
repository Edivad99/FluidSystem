package edivad.fluidsystem.blockentity.tank;

import edivad.fluidsystem.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public class InterfaceTankBlockEntity extends BaseTankBlockEntity {

    public InterfaceTankBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.INTERFACE_TANK_BLOCK_TILE.get(), pos, state);
    }

    @Override
    public boolean isMaster() {
        return false;
    }

    @Override
    public int blockCapacity() {
        return 0;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap.equals(ForgeCapabilities.FLUID_HANDLER)) {
            ControllerTankBlockEntity controller = (ControllerTankBlockEntity) getMaster();
            if(controller != null)
                return controller.getFluidCap().cast();
        }
        return super.getCapability(cap, side);
    }
}
