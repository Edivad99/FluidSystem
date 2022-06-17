package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import edivad.fluidsystem.tools.Config;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.text.DecimalFormat;

public class BaseTankBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof BaseTankBlockEntity) {
            CompoundTag data = accessor.getServerData();
            if(data.getBoolean("isControllerPresent")) {
                int numberOfBlocks = data.getInt("numberOfTanksBlock");
                String percentage = String.format("%d/%d", numberOfBlocks, Config.NUMBER_OF_MODULES.get());
                tooltip.add(Component.translatable(Translations.TANKS_BLOCK).append(percentage));

                if(data.getBoolean("canReadLiquid")) {
                    int fluidAmount = data.getInt("fluidAmount");
                    if(fluidAmount > 0) {
                        String fluidName = Component.translatable(data.getString("fluid")).getString();
                        DecimalFormat f = new DecimalFormat("#,##0");
                        tooltip.add(Component.literal(String.format("%s: %smB", fluidName, f.format(fluidAmount))));
                    } else {
                        tooltip.add(Component.translatable(Translations.TANK_EMPTY).append(": 0mB"));
                    }

                }
            }
        }
    }

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
