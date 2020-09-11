package edivad.fluidsystem.tile.pipe;

import edivad.fluidsystem.api.IFluidSystemEject;
import edivad.fluidsystem.blocks.pipe.BlockInputPipe;
import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Routing;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class TileEntityBlockInputPipe extends TileEntityBlockFilterablePipe implements ITickableTileEntity
{
    public TileEntityBlockInputPipe()
    {
        super(Registration.INPUT_PIPE_TILE.get());
    }

    @Override
    public void tick()
    {
        if(world.isRemote)
            return;

        if(this.getBlockState().get(BlockInputPipe.POWERED))
        {
            Direction inputFace = this.getBlockState().get(BlockInputPipe.FACING).getOpposite();
            TileEntity tile = world.getTileEntity(getPos().offset(inputFace));
            if(tile != null)
            {
                tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, inputFace.getOpposite()).ifPresent(input ->
                {
                    for(int i = 0; i < input.getTanks(); i++)
                    {
                        FluidStack inputFluidStack = input.getFluidInTank(i);
                        if(!inputFluidStack.isEmpty() && (getFluidFilter().isEquivalentTo(Fluids.EMPTY) || getFluidFilter().isEquivalentTo(inputFluidStack.getFluid())))
                        {
                            Fluid fluid = inputFluidStack.getFluid();
                            List<IFluidSystemEject> outputs = Routing.getBlockEject(world, getPos(), getPos().offset(this.getBlockState().get(BlockInputPipe.FACING)));

                            int amountInput = inputFluidStack.getAmount();
                            for(IFluidSystemEject output : outputs)
                            {
                                if(output.acceptFluid(fluid) && amountInput > 0)
                                {
                                    FluidStack test = input.drain(amountInput, IFluidHandler.FluidAction.SIMULATE);
                                    int fluidAmountInserted = output.fill(test, IFluidHandler.FluidAction.SIMULATE);
                                    if(fluidAmountInserted >= 0)
                                    {
                                        FluidStack fluidExtracted = input.drain(fluidAmountInserted, IFluidHandler.FluidAction.EXECUTE);
                                        int fluidAmountInsertedEffect = output.fill(fluidExtracted, IFluidHandler.FluidAction.EXECUTE);
                                        amountInput = Math.max(0, amountInput - fluidAmountInsertedEffect);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}
