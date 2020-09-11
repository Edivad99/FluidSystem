package edivad.fluidsystem.tile.pipe;

import edivad.fluidsystem.api.IFluidSystemFilterable;
import edivad.fluidsystem.network.PacketHandler;
import edivad.fluidsystem.network.packet.UpdateBlockFilterablePipe;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.PacketDistributor;

public class TileEntityBlockFilterablePipe extends TileEntity implements IFluidSystemFilterable
{
    private FluidStack fluidFilter = FluidStack.EMPTY;

    public TileEntityBlockFilterablePipe(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        fluidFilter.writeToNBT(tag);
        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag)
    {
        super.read(state, tag);
        fluidFilter = FluidStack.loadFluidStackFromNBT(tag);
    }

    //Synchronizing on block update
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT tag = new CompoundNBT();
        fluidFilter.writeToNBT(tag);
        return new SUpdateTileEntityPacket(getPos(), 1 , tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        CompoundNBT tag = pkt.getNbtCompound();
        fluidFilter = FluidStack.loadFluidStackFromNBT(tag);
    }

    //Synchronizing on chunk load
    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT tag = super.getUpdateTag();
        fluidFilter.writeToNBT(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        super.handleUpdateTag(state, tag);
        fluidFilter = FluidStack.loadFluidStackFromNBT(tag);
    }

    @Override
    public void setFilteredFluid(Fluid fluid)
    {
        if(fluid.isEquivalentTo(Fluids.EMPTY))
            fluidFilter = FluidStack.EMPTY;
        else
            fluidFilter = new FluidStack(fluid, 1000);
        markDirty();
        if(!world.isRemote)
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UpdateBlockFilterablePipe(getPos(), fluidFilter));
    }

    @Override
    public Fluid getFluidFilter()
    {
        return fluidFilter.getFluid();
    }
}
