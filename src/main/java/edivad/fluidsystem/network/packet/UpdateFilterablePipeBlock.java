package edivad.fluidsystem.network.packet;

import edivad.edivadlib.network.EdivadLibPacket;
import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record UpdateFilterablePipeBlock(
    BlockPos pos, FluidStack fluidStack) implements EdivadLibPacket {

  public static final ResourceLocation ID = FluidSystem.rl("update_filterable_pipe_block");

  public static UpdateFilterablePipeBlock read(FriendlyByteBuf buf) {
    var pos = buf.readBlockPos();
    var fluidStack = buf.readFluidStack();
    return new UpdateFilterablePipeBlock(pos, fluidStack);
  }

  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    buf.writeFluidStack(fluidStack);
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
        if (be instanceof FilterablePipeBlockEntity output) {
          output.setFilteredFluid(fluidStack.getFluid());
        }
      }
    });
  }
}
