package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.tile.tank.TileEntityBaseTankBlock;
import edivad.fluidsystem.tools.Config;
import edivad.fluidsystem.tools.Translations;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class BaseTankBlockComponentProvider implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if(accessor.getTileEntity() instanceof TileEntityBaseTankBlock)
        {
            CompoundNBT data = accessor.getServerData();
            if(data.getBoolean("isControllerPresent"))
            {
                tooltip.add(new TranslationTextComponent(Translations.TANKS_BLOCK).appendString(String.format("%d/%d", data.getInt("numberOfTanksBlock"), Config.NUMBER_OF_MODULES.get())));

                if(data.getBoolean("canReadLiquid"))
                {
                    tooltip.add(new StringTextComponent(data.getString("fluidInside")));
                }
            }
        }
    }
}
