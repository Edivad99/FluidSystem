package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.tile.tank.TileEntityBaseTankBlock;
import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;

public class BaseTankBlockDataProvider implements IServerDataProvider<TileEntity>
{

    @Override
    public void appendServerData(CompoundNBT data, ServerPlayerEntity player, World world, TileEntity tileEntity)
    {
        if(tileEntity instanceof TileEntityBaseTankBlock)
        {
            TileEntityBaseTankBlock tankBase = ((TileEntityBaseTankBlock) tileEntity).getMaster();
            if(tankBase != null)
            {
                data.putBoolean("isControllerPresent", true);
                TileEntityControllerTankBlock controller = (TileEntityControllerTankBlock) tankBase;
                int numberOfTanksBlock = controller.getNumberOfTanksBlock();
                data.putInt("numberOfTanksBlock", numberOfTanksBlock);

                data.putBoolean("canReadLiquid", controller.getFluidCap().isPresent());
                controller.getFluidCap().ifPresent(h ->
                {
                    FluidStack fluidStack = h.getFluidInTank(0);
                    String fluidName = fluidStack.isEmpty() ? "Empty" : fluidStack.getDisplayName().getString();
                    DecimalFormat f = new DecimalFormat("#,##0");
                    int amount = fluidStack.getAmount();
                    data.putString("fluidInside", String.format("%s: %smB", fluidName, f.format(amount)));
                });
            }
            else
            {
                data.putBoolean("isControllerPresent", false);
            }
        }
    }
}
