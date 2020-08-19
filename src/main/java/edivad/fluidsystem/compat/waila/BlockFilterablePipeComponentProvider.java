package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.tile.pipe.TileEntityBlockFilterablePipe;
import edivad.fluidsystem.tools.Translations;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class BlockFilterablePipeComponentProvider implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if(accessor.getTileEntity() instanceof TileEntityBlockFilterablePipe)
        {
            CompoundNBT data = accessor.getServerData();
            if(data.getBoolean("isFluidPresent"))
            {
                String fluidName = data.getString("fluid");
                tooltip.add(new TranslationTextComponent(Translations.FLUID_FILTERED, fluidName));
            }
        }
    }
}
