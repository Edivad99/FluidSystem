package edivad.fluidsystem.network.packet;

import java.util.function.Supplier;
import edivad.fluidsystem.network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

public record UpdateFilterablePipeBlock(BlockPos pos, FluidStack fluidStack) {

  public static UpdateFilterablePipeBlock decode(FriendlyByteBuf buf) {
    var pos = buf.readBlockPos();
    var fluidStack = buf.readFluidStack();
    return new UpdateFilterablePipeBlock(pos, fluidStack);
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    buf.writeFluidStack(fluidStack);
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      if (FMLEnvironment.dist == Dist.CLIENT) {
        ClientPacketHandler.updateFilterableBlock(pos, fluidStack);
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
