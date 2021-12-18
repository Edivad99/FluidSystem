package edivad.fluidsystem.network.packet;

import edivad.fluidsystem.network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateControllerTankBlock {

    private final BlockPos pos;
    private final FluidStack fluidStack;
    private final int tanksBlock;
    private final int totalCapacity;

    public UpdateControllerTankBlock(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        fluidStack = buf.readFluidStack();
        tanksBlock = buf.readVarInt();
        totalCapacity = buf.readVarInt();
    }

    public UpdateControllerTankBlock(BlockPos pos, FluidStack fluidStack, int tanksBlock, int totalCapacity) {
        this.pos = pos;
        this.fluidStack = fluidStack;
        this.tanksBlock = tanksBlock;
        this.totalCapacity = totalCapacity;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeFluidStack(fluidStack);
        buf.writeVarInt(tanksBlock);
        buf.writeVarInt(totalCapacity);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.updateTankBlock(pos, fluidStack, tanksBlock, totalCapacity)));
        ctx.get().setPacketHandled(true);
    }
}
