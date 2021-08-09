package edivad.fluidsystem.network.packet;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.tile.pipe.TileEntityBlockFilterablePipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateBlockFilterablePipe {

    private final BlockPos pos;
    private final FluidStack fluidStack;

    public UpdateBlockFilterablePipe(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        fluidStack = buf.readFluidStack();
    }

    public UpdateBlockFilterablePipe(BlockPos pos, FluidStack fluidStack) {
        this.pos = pos;
        this.fluidStack = fluidStack;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeFluidStack(fluidStack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level world = Main.proxy.getClientLevel();
            if(world.isLoaded(pos)) {
                BlockEntity te = world.getBlockEntity(pos);
                if(te instanceof TileEntityBlockFilterablePipe output) {
                    output.setFilteredFluid(fluidStack.getFluid());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
