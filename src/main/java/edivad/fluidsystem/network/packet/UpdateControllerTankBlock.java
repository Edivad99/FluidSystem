package edivad.fluidsystem.network.packet;

import edivad.edivadlib.network.EdivadLibPacket;
import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record UpdateControllerTankBlock(
    BlockPos pos, FluidStack fluidStack,
    int tanksBlock, int totalCapacity) implements EdivadLibPacket {

  public static final ResourceLocation ID = FluidSystem.rl("update_controller_tank_block");

  public static UpdateControllerTankBlock read(FriendlyByteBuf buf) {
    var pos = buf.readBlockPos();
    var fluidStack = buf.readFluidStack();
    var tanksBlock = buf.readVarInt();
    var totalCapacity = buf.readVarInt();
    return new UpdateControllerTankBlock(pos, fluidStack, tanksBlock, totalCapacity);
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    buf.writeFluidStack(fluidStack);
    buf.writeVarInt(tanksBlock);
    buf.writeVarInt(totalCapacity);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext playPayloadContext) {
    playPayloadContext.level().ifPresent(level -> {
      if (level.isLoaded(pos)) {
        var be = level.getBlockEntity(pos);
        if (be instanceof ControllerTankBlockEntity controller) {
          controller.clientFluidStack = fluidStack;
          controller.tanksBlock = tanksBlock;
          controller.totalCapacity = totalCapacity;
        }
      }
    });
  }
}
