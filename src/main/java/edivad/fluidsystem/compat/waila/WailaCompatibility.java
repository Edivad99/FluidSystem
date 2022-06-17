package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.InputTankBlockEntity;
import edivad.fluidsystem.blocks.pipe.FilterableBlock;
import edivad.fluidsystem.blocks.tank.BaseBlock;
import edivad.fluidsystem.blocks.tank.InputTankBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(Main.MODID)
public class WailaCompatibility implements IWailaPlugin {

    private static final FilterablePipeBlockComponentProvider FILTERABLE_PIPE_COMPONENT_PROVIDER = new FilterablePipeBlockComponentProvider();
    private static final BaseTankBlockComponentProvider BASE_TANK_BLOCK_COMPONENT_PROVIDER = new BaseTankBlockComponentProvider();

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(FILTERABLE_PIPE_COMPONENT_PROVIDER, FilterablePipeBlockEntity.class);
        registration.registerBlockDataProvider(BASE_TANK_BLOCK_COMPONENT_PROVIDER, BaseTankBlockEntity.class);
        registration.registerBlockDataProvider(BASE_TANK_BLOCK_COMPONENT_PROVIDER, InputTankBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(FILTERABLE_PIPE_COMPONENT_PROVIDER, FilterableBlock.class);
        registration.registerBlockComponent(BASE_TANK_BLOCK_COMPONENT_PROVIDER, BaseBlock.class);
        registration.registerBlockComponent(BASE_TANK_BLOCK_COMPONENT_PROVIDER, InputTankBlock.class);
    }
}
