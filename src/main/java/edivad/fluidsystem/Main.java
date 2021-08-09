package edivad.fluidsystem;

import edivad.fluidsystem.setup.ClientSetup;
import edivad.fluidsystem.setup.ModSetup;
import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.setup.proxy.IProxy;
import edivad.fluidsystem.setup.proxy.Proxy;
import edivad.fluidsystem.setup.proxy.ProxyClient;
import edivad.fluidsystem.tools.Config;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {

    public static final String MODID = "fluidsystem";
    public static final String MODNAME = "FluidSystem";
    public static final Logger logger = LogManager.getLogger();
    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ProxyClient::new, () -> Proxy::new);

    public Main() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        Registration.init();

        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ModSetup::init);
        eventBus.addListener(ClientSetup::init);
    }
}
