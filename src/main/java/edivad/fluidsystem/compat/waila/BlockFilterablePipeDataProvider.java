package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.tile.pipe.TileEntityBlockFilterablePipe;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFilterablePipeDataProvider implements IServerDataProvider<TileEntity> {

    @Override
    public void appendServerData(CompoundNBT data, ServerPlayerEntity player, World world, TileEntity tileEntity)
    {
        if(tileEntity instanceof TileEntityBlockFilterablePipe)
        {
            TileEntityBlockFilterablePipe output = (TileEntityBlockFilterablePipe) tileEntity;
            Fluid fluid = output.getFluidFilter();
            if(!fluid.isEquivalentTo(Fluids.EMPTY))
            {
                data.putBoolean("isFluidPresent", true);
                data.putString("fluid", fluid.getAttributes().getDisplayName(null).getString());
            }
            else
                data.putBoolean("isFluidPresent", false);
        }
    }
}
