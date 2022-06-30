package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class FilterablePipeBlockComponent implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof FilterablePipeBlockEntity) {
            CompoundTag data = accessor.getServerData();
            if(data.getBoolean("isFluidPresent")) {
                String fluidName = Component.translatable(data.getString("fluid")).getString();
                tooltip.add(Component.translatable(Translations.FLUID_FILTERED).append(fluidName));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "filterable_pipe_block");
    }
}
