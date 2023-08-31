package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class BaseTankBlockProvider implements IServerDataProvider<BlockAccessor> {

  @Override
  public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
    if (blockAccessor.getBlockEntity() instanceof BaseTankBlockEntity tankBlock) {
      BaseTankBlockEntity tankBase = tankBlock.getMaster();
      if (tankBase != null) {
        compoundTag.putBoolean("isControllerPresent", true);
        ControllerTankBlockEntity controller = (ControllerTankBlockEntity) tankBase;
        int numberOfTanksBlock = controller.getNumberOfTanksBlock();
        compoundTag.putInt("numberOfTanksBlock", numberOfTanksBlock);

        compoundTag.putBoolean("canReadLiquid", controller.getFluidCap().isPresent());
        controller.getFluidCap().ifPresent(h -> {
          FluidStack fluidStack = h.getFluidInTank(0);
          if (!fluidStack.isEmpty()) {
            compoundTag.putString("fluid", fluidStack.getFluid().getFluidType().getDescriptionId());
          }
          compoundTag.putInt("fluidAmount", fluidStack.getAmount());
        });
      } else {
        compoundTag.putBoolean("isControllerPresent", false);
      }
    }
  }

  @Override
  public ResourceLocation getUid() {
    return new ResourceLocation(FluidSystem.ID, "base_tank_block");
  }
}
