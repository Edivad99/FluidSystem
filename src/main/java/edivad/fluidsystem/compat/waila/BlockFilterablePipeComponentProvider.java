package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.tile.pipe.TileEntityBlockFilterablePipe;
import edivad.fluidsystem.tools.Translations;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;

public class BlockFilterablePipeComponentProvider implements IComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof TileEntityBlockFilterablePipe) {
            CompoundTag data = accessor.getServerData();
            if(data.getBoolean("isFluidPresent")) {
                String fluidName = data.getString("fluid");
                tooltip.add(new TranslatableComponent(Translations.FLUID_FILTERED, fluidName));
            }
        }
    }
}
