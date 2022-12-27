package edivad.fluidsystem;

import com.mojang.logging.LogUtils;
import edivad.fluidsystem.setup.ClientSetup;
import edivad.fluidsystem.setup.Config;
import edivad.fluidsystem.setup.ModSetup;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main {

    public static final String MODID = "fluidsystem";
    public static final String MODNAME = "FluidSystem";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        Registration.init();
        Config.init();

        // Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModSetup::init);
        modEventBus.addListener(ClientSetup::init);
        modEventBus.addListener(Main::onCreativeModeTabRegister);
    }

    private static void onCreativeModeTabRegister(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MODID, "tab"),
                builder -> builder
                        .icon(() -> new ItemStack(Registration.INFINITE_WATER_SOURCE_ITEM.get()))
                        .title(Component.literal(MODNAME))
                        .displayItems((features, output, hasPermissions) -> {
                            output.accept(new ItemStack(Registration.INFINITE_WATER_SOURCE_ITEM.get()));
                            output.accept(new ItemStack(Registration.PIPE_ITEM.get()));
                            output.accept(new ItemStack(Registration.PIPE_CONTROLLER_ITEM.get()));
                            output.accept(new ItemStack(Registration.INPUT_PIPE_ITEM.get()));
                            output.accept(new ItemStack(Registration.OUTPUT_PIPE_ITEM.get()));
                            output.accept(new ItemStack(Registration.STRUCTURAL_TANK_BLOCK_ITEM.get()));
                            output.accept(new ItemStack(Registration.INTERFACE_TANK_BLOCK_ITEM.get()));
                            output.accept(new ItemStack(Registration.INPUT_TANK_BLOCK_ITEM.get()));
                            output.accept(new ItemStack(Registration.CONTROLLER_TANK_BLOCK_ITEM.get()));
                        }));
    }
}
