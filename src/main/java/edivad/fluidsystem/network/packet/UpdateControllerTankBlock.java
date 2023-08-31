package edivad.fluidsystem.network.packet;

import java.util.function.Supplier;
import edivad.fluidsystem.network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

public record UpdateControllerTankBlock(BlockPos pos, FluidStack fluidStack, int tanksBlock,
                                        int totalCapacity) {

  public static UpdateControllerTankBlock decode(FriendlyByteBuf buf) {
    var pos = buf.readBlockPos();
    var fluidStack = buf.readFluidStack();
    var tanksBlock = buf.readVarInt();
    var totalCapacity = buf.readVarInt();
    return new UpdateControllerTankBlock(pos, fluidStack, tanksBlock, totalCapacity);
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeBlockPos(pos);
    buf.writeFluidStack(fluidStack);
    buf.writeVarInt(tanksBlock);
    buf.writeVarInt(totalCapacity);
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      if (FMLEnvironment.dist == Dist.CLIENT) {
        ClientPacketHandler.updateTankBlock(pos, fluidStack, tanksBlock, totalCapacity);
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
