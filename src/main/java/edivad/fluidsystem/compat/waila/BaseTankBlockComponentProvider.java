package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.tile.tank.TileEntityBaseTankBlock;
import edivad.fluidsystem.tools.Config;
import edivad.fluidsystem.tools.Translations;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class BaseTankBlockComponentProvider implements IComponentProvider
{
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        if(accessor.getBlockEntity() instanceof TileEntityBaseTankBlock)
        {
            CompoundTag data = accessor.getServerData();
            if(data.getBoolean("isControllerPresent"))
            {
                tooltip.add(new TranslatableComponent(Translations.TANKS_BLOCK).append(String.format("%d/%d", data.getInt("numberOfTanksBlock"), Config.NUMBER_OF_MODULES.get())));

                if(data.getBoolean("canReadLiquid"))
                {
                    tooltip.add(new TextComponent(data.getString("fluidInside")));
                }
            }
        }
    }
}
