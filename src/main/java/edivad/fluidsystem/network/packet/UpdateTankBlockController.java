package edivad.fluidsystem.network.packet;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateTankBlockController
{
    private final BlockPos pos;
    private final FluidStack fluidStack;
    private final int tanksBlock;
    private final int totalCapacity;

    public UpdateTankBlockController(FriendlyByteBuf buf)
    {
        pos = buf.readBlockPos();
        fluidStack = buf.readFluidStack();
        tanksBlock = buf.readVarInt();
        totalCapacity = buf.readVarInt();
    }

    public UpdateTankBlockController(BlockPos pos, FluidStack fluidStack, int tanksBlock, int totalCapacity)
    {
        this.pos = pos;
        this.fluidStack = fluidStack;
        this.tanksBlock = tanksBlock;
        this.totalCapacity = totalCapacity;
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(pos);
        buf.writeFluidStack(fluidStack);
        buf.writeVarInt(tanksBlock);
        buf.writeVarInt(totalCapacity);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            Level world = Main.proxy.getClientLevel();
            if(world.isLoaded(pos))
            {
                BlockEntity te = world.getBlockEntity(pos);
                if(te instanceof TileEntityControllerTankBlock controller)
                {
                    controller.clientFluidStack = fluidStack;
                    controller.tanksBlock = tanksBlock;
                    controller.totalCapacity = totalCapacity;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
