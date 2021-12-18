package edivad.fluidsystem.network.packet;

import edivad.fluidsystem.network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateFilterablePipeBlock {

    private final BlockPos pos;
    private final FluidStack fluidStack;

    public UpdateFilterablePipeBlock(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        fluidStack = buf.readFluidStack();
    }

    public UpdateFilterablePipeBlock(BlockPos pos, FluidStack fluidStack) {
        this.pos = pos;
        this.fluidStack = fluidStack;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeFluidStack(fluidStack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.updateFilterableBlock(pos, fluidStack)));
        ctx.get().setPacketHandled(true);
    }
}
