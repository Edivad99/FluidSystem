package edivad.fluidsystem.network;

import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

public class ClientPacketHandler {

  public static void updateTankBlock(BlockPos pos, FluidStack fluidStack, int tanksBlock,
      int totalCapacity) {
    Level level = Minecraft.getInstance().level;
    if (level.isLoaded(pos)) {
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof ControllerTankBlockEntity controller) {
        controller.clientFluidStack = fluidStack;
        controller.tanksBlock = tanksBlock;
        controller.totalCapacity = totalCapacity;
      }
    }
  }

  public static void updateFilterableBlock(BlockPos pos, FluidStack fluidStack) {
    Level level = Minecraft.getInstance().level;
    if (level.isLoaded(pos)) {
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof FilterablePipeBlockEntity output) {
        output.setFilteredFluid(fluidStack.getFluid());
      }
    }
  }
}
