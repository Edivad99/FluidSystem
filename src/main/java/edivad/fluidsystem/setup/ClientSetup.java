package edivad.fluidsystem.setup;

import edivad.fluidsystem.client.render.RenderTileBlockFilterablePipe;
import edivad.fluidsystem.client.screen.ScreenModularTank;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup
{

    public static void init(FMLClientSetupEvent event)
    {
        //Version checker
        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);

        //Special render & GUI
        ScreenManager.registerFactory(Registration.CONTROLLER_TANK_BLOCK_CONTAINER.get(), ScreenModularTank::new);

        ClientRegistry.bindTileEntityRenderer(Registration.OUTPUT_PIPE_TILE.get(), RenderTileBlockFilterablePipe::new);
        ClientRegistry.bindTileEntityRenderer(Registration.INPUT_PIPE_TILE.get(), RenderTileBlockFilterablePipe::new);
    }
}