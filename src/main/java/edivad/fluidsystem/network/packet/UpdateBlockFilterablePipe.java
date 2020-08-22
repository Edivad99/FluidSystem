package edivad.fluidsystem.network.packet;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.tile.pipe.TileEntityBlockFilterablePipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateBlockFilterablePipe
{

    private final BlockPos pos;
    private final FluidStack fluidStack;

    public UpdateBlockFilterablePipe(PacketBuffer buf)
    {
        pos = buf.readBlockPos();
        fluidStack = buf.readFluidStack();
    }

    public UpdateBlockFilterablePipe(BlockPos pos, FluidStack fluidStack)
    {
        this.pos = pos;
        this.fluidStack = fluidStack;
    }

    public void toBytes(PacketBuffer buf)
    {
        buf.writeBlockPos(pos);
        buf.writeFluidStack(fluidStack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            World world = Main.proxy.getClientWorld();
            if(world.isBlockPresent(pos))
            {
                TileEntity te = world.getTileEntity(pos);
                if(te instanceof TileEntityBlockFilterablePipe)
                {
                    TileEntityBlockFilterablePipe output = (TileEntityBlockFilterablePipe) te;
                    output.setClientFluid(fluidStack);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
