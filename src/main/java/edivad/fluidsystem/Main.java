package edivad.fluidsystem;

import com.mojang.logging.LogUtils;
import edivad.fluidsystem.setup.ClientSetup;
import edivad.fluidsystem.setup.ModSetup;
import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Config;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {

    public static final String MODID = "fluidsystem";
    public static final String MODNAME = "FluidSystem";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        Registration.init();

        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ModSetup::init);
        eventBus.addListener(ClientSetup::init);
    }
}
