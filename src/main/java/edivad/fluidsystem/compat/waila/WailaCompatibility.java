package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blocks.pipe.BlockFilterable;
import edivad.fluidsystem.blocks.tank.BaseBlock;
import edivad.fluidsystem.blocks.tank.InputTankBlock;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(Main.MODID)
public class WailaCompatibility implements IWailaPlugin
{
    @Override
    public void register(IRegistrar registrar)
    {
        registrar.registerBlockDataProvider(new BlockFilterablePipeDataProvider(), BlockFilterable.class);
        registrar.registerComponentProvider(new BlockFilterablePipeComponentProvider(), TooltipPosition.BODY, BlockFilterable.class);

        registrar.registerBlockDataProvider(new BaseTankBlockDataProvider(), BaseBlock.class);
        registrar.registerComponentProvider(new BaseTankBlockComponentProvider(), TooltipPosition.BODY, BaseBlock.class);
        registrar.registerBlockDataProvider(new BaseTankBlockDataProvider(), InputTankBlock.class);
        registrar.registerComponentProvider(new BaseTankBlockComponentProvider(), TooltipPosition.BODY, InputTankBlock.class);
    }
}
