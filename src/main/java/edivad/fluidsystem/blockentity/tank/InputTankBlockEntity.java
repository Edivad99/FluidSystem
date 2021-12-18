package edivad.fluidsystem.blockentity.tank;

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

public class InputTankBlockEntity extends BaseTankBlockEntity implements IFluidSystemEject {

    public InputTankBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.INPUT_TANK_BLOCK_TILE.get(), pos, state);
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
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if(acceptFluid(resource.getFluid())) {
            ControllerTankBlockEntity controller = (ControllerTankBlockEntity) getMaster();
            if(controller != null) {
                AtomicInteger res = new AtomicInteger(0);
                controller.getFluidCap().ifPresent(h -> {
                    res.set(h.fill(resource, action));
                });
                return res.get();
            }
        }
        return 0;
    }

    @Override
    public boolean acceptFluid(Fluid fluidToInsert) {
        ControllerTankBlockEntity controller = (ControllerTankBlockEntity) getMaster();
        if(controller != null) {
            AtomicBoolean result = new AtomicBoolean(false);
            controller.getFluidCap().ifPresent(h -> {
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
