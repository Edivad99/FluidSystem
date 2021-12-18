package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;

public class BaseTankBlockDataProvider implements IServerDataProvider<BlockEntity> {

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity tileEntity, boolean showDetails) {
        if(tileEntity instanceof BaseTankBlockEntity tankBlock) {
            BaseTankBlockEntity tankBase = tankBlock.getMaster();
            if(tankBase != null) {
                data.putBoolean("isControllerPresent", true);
                ControllerTankBlockEntity controller = (ControllerTankBlockEntity) tankBase;
                int numberOfTanksBlock = controller.getNumberOfTanksBlock();
                data.putInt("numberOfTanksBlock", numberOfTanksBlock);

                data.putBoolean("canReadLiquid", controller.getFluidCap().isPresent());
                controller.getFluidCap().ifPresent(h -> {
                    FluidStack fluidStack = h.getFluidInTank(0);
                    String fluidName = fluidStack.isEmpty() ? "Empty" : fluidStack.getDisplayName().getString();
                    DecimalFormat f = new DecimalFormat("#,##0");
                    int amount = fluidStack.getAmount();
                    data.putString("fluidInside", String.format("%s: %smB", fluidName, f.format(amount)));
                });
            }
            else {
                data.putBoolean("isControllerPresent", false);
            }
        }
    }
}
