package edivad.fluidsystem.tile;

import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.InfiniteTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityInfinityWaterSource extends BlockEntity
{
    private final InfiniteTank tank = new InfiniteTank(Fluids.WATER);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    public TileEntityInfinityWaterSource(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.INFINITE_WATER_SOURCE_TILE.get(), blockPos, blockState);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
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
