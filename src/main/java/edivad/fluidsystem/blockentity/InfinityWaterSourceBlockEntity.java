package edivad.fluidsystem.blockentity;

import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.InfiniteTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfinityWaterSourceBlockEntity extends BlockEntity {

    private final InfiniteTank tank = new InfiniteTank(Fluids.WATER);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    public InfinityWaterSourceBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.INFINITE_WATER_SOURCE_TILE.get(), pos, state);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY))
            return fluidHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        fluidHandler.invalidate();
    }
}
