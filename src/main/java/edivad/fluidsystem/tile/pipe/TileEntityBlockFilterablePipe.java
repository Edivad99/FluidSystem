package edivad.fluidsystem.tile.pipe;

import edivad.fluidsystem.api.IFluidSystemFilterable;
import edivad.fluidsystem.network.PacketHandler;
import edivad.fluidsystem.network.packet.UpdateBlockFilterablePipe;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.PacketDistributor;

public class TileEntityBlockFilterablePipe extends TileEntity implements ITickableTileEntity, IFluidSystemFilterable {

    private FluidStack fluidFilter = FluidStack.EMPTY;
    private FluidStack clientFluid = FluidStack.EMPTY;

    public TileEntityBlockFilterablePipe(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        if(world.isRemote)
            return;

        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UpdateBlockFilterablePipe(getPos(), fluidFilter));
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

    @Override
    public void setFilteredFluid(Fluid fluid)
    {
        if(fluid.isEquivalentTo(Fluids.EMPTY))
            fluidFilter = FluidStack.EMPTY;
        else
            fluidFilter = new FluidStack(fluid, 1000);
        markDirty();
    }

    @Override
    public Fluid getFluidFilter()
    {
        return fluidFilter.getFluid();
    }

    //Client
    public Fluid getClientFluid()
    {
        return clientFluid.getFluid();
    }

    public void setClientFluid(FluidStack fluidStack)
    {
        clientFluid = fluidStack;
    }
}
