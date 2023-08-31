package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.FluidSystem;
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

@WailaPlugin(FluidSystem.ID)
public class WailaCompatibility implements IWailaPlugin {

  @Override
  public void register(IWailaCommonRegistration registration) {
    registration.registerBlockDataProvider(new FilterablePipeBlockProvider(),
        FilterablePipeBlockEntity.class);
    registration.registerBlockDataProvider(new BaseTankBlockProvider(), BaseTankBlockEntity.class);
    registration.registerBlockDataProvider(new BaseTankBlockProvider(), InputTankBlockEntity.class);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerBlockComponent(new FilterablePipeBlockComponent(), FilterableBlock.class);
    registration.registerBlockComponent(new BaseTankBlockComponent(), BaseBlock.class);
    registration.registerBlockComponent(new BaseTankBlockComponent(), InputTankBlock.class);
  }
}
