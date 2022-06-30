package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import edivad.fluidsystem.setup.Config;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.text.DecimalFormat;

public class BaseTankBlockComponent implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof BaseTankBlockEntity) {
            CompoundTag data = accessor.getServerData();
            if(data.getBoolean("isControllerPresent")) {
                int numberOfBlocks = data.getInt("numberOfTanksBlock");
                String percentage = String.format("%d/%d", numberOfBlocks, Config.Tank.NUMBER_OF_MODULES.get());
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
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "base_tank_block");
    }
}
