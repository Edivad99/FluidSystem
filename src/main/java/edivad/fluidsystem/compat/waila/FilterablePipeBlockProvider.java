package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class FilterablePipeBlockProvider implements IServerDataProvider<BlockAccessor> {

  @Override
  public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
    if (blockAccessor.getBlockEntity() instanceof FilterablePipeBlockEntity output) {
      Fluid fluid = output.getFluidFilter();
      if (!fluid.isSame(Fluids.EMPTY)) {
        compoundTag.putBoolean("isFluidPresent", true);
        compoundTag.putString("fluid", fluid.getFluidType().getDescriptionId());
      } else {
        compoundTag.putBoolean("isFluidPresent", false);
      }
    }
  }

  @Override
  public ResourceLocation getUid() {
    return new ResourceLocation(FluidSystem.ID, "filterable_pipe_block");
  }
}
