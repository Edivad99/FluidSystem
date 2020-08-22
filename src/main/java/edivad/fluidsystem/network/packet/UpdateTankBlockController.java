package edivad.fluidsystem.network.packet;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateTankBlockController
{

    private final BlockPos pos;
    private final FluidStack fluidStack;
    private final int tanksBlock;
    private final int totalCapacity;

    public UpdateTankBlockController(PacketBuffer buf)
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

    public void toBytes(PacketBuffer buf)
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
            World world = Main.proxy.getClientWorld();
            if(world.isBlockPresent(pos))
            {
                TileEntity te = world.getTileEntity(pos);
                if(te instanceof TileEntityControllerTankBlock)
                {
                    TileEntityControllerTankBlock controller = (TileEntityControllerTankBlock) te;
                    controller.clientFluidStack = fluidStack;
                    controller.tanksBlock = tanksBlock;
                    controller.totalCapacity = totalCapacity;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
