package edivad.fluidsystem.setup;

import edivad.fluidsystem.client.render.RenderFilterablePipeBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ClientSetup {

  public static void init(IEventBus modEventBus) {
    modEventBus.addListener(ClientSetup::registerRenders);
  }

  private static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(Registration.OUTPUT_PIPE_BLOCK_ENTITY.get(),
        RenderFilterablePipeBlockEntity::new);
    event.registerBlockEntityRenderer(Registration.INPUT_PIPE_BLOCK_ENTITY.get(),
        RenderFilterablePipeBlockEntity::new);
  }
}
