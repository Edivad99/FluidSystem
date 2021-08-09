package edivad.fluidsystem.setup;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.client.render.RenderTileBlockFilterablePipe;
import edivad.fluidsystem.client.screen.ScreenModularTank;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
    public static void init(FMLClientSetupEvent event)
    {
        //Version checker
        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);

        //Special render & GUI
        MenuScreens.register(Registration.CONTROLLER_TANK_BLOCK_CONTAINER.get(), ScreenModularTank::new);

        //ClientRegistry.bindTileEntityRenderer(Registration.OUTPUT_PIPE_TILE.get(), RenderTileBlockFilterablePipe::new);
        //ClientRegistry.bindTileEntityRenderer(Registration.INPUT_PIPE_TILE.get(), RenderTileBlockFilterablePipe::new);
    }

    @SubscribeEvent
    public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registration.OUTPUT_PIPE_TILE.get(), RenderTileBlockFilterablePipe::new);
        event.registerBlockEntityRenderer(Registration.INPUT_PIPE_TILE.get(), RenderTileBlockFilterablePipe::new);
    }
}