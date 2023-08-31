package edivad.fluidsystem.setup;

import edivad.fluidsystem.client.render.RenderFilterablePipeBlockEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientSetup {

  public static void init(IEventBus modEventBus) {
    modEventBus.addListener(ClientSetup::registerRenders);
  }

  private static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(Registration.OUTPUT_PIPE_TILE.get(),
        RenderFilterablePipeBlockEntity::new);
    event.registerBlockEntityRenderer(Registration.INPUT_PIPE_TILE.get(),
        RenderFilterablePipeBlockEntity::new);
  }
}