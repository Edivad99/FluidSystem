package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.IServerDataProvider;

public class BaseTankBlockProvider implements IServerDataProvider<BlockEntity> {

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if(blockEntity instanceof BaseTankBlockEntity tankBlock) {
            BaseTankBlockEntity tankBase = tankBlock.getMaster();
            if(tankBase != null) {
                compoundTag.putBoolean("isControllerPresent", true);
                ControllerTankBlockEntity controller = (ControllerTankBlockEntity) tankBase;
                int numberOfTanksBlock = controller.getNumberOfTanksBlock();
                compoundTag.putInt("numberOfTanksBlock", numberOfTanksBlock);

                compoundTag.putBoolean("canReadLiquid", controller.getFluidCap().isPresent());
                controller.getFluidCap().ifPresent(h -> {
                    FluidStack fluidStack = h.getFluidInTank(0);
                    if(!fluidStack.isEmpty()) {
                        compoundTag.putString("fluid", fluidStack.getFluid().getFluidType().getDescriptionId());
                    }
                    compoundTag.putInt("fluidAmount", fluidStack.getAmount());
                });
            }
            else {
                compoundTag.putBoolean("isControllerPresent", false);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "base_tank_block");
    }
}
