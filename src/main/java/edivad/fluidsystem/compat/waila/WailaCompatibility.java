package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.InputTankBlockEntity;
import edivad.fluidsystem.blocks.pipe.FilterableBlock;
import edivad.fluidsystem.blocks.tank.BaseBlock;
import edivad.fluidsystem.blocks.tank.InputTankBlock;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(Main.MODID)
public class WailaCompatibility implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerBlockDataProvider(new BlockFilterablePipeDataProvider(), FilterablePipeBlockEntity.class);
        registrar.registerComponentProvider(new BlockFilterablePipeComponentProvider(), TooltipPosition.BODY, FilterableBlock.class);

        registrar.registerBlockDataProvider(new BaseTankBlockDataProvider(), BaseTankBlockEntity.class);
        registrar.registerComponentProvider(new BaseTankBlockComponentProvider(), TooltipPosition.BODY, BaseBlock.class);
        registrar.registerBlockDataProvider(new BaseTankBlockDataProvider(), InputTankBlockEntity.class);
        registrar.registerComponentProvider(new BaseTankBlockComponentProvider(), TooltipPosition.BODY, InputTankBlock.class);
    }
}
