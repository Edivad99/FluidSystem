package edivad.fluidsystem;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import edivad.edivadlib.setup.UpdateChecker;
import edivad.fluidsystem.client.screen.ScreenModularTank;
import edivad.fluidsystem.compat.top.TOPProvider;
import edivad.fluidsystem.datagen.BlockStates;
import edivad.fluidsystem.datagen.Items;
import edivad.fluidsystem.datagen.Lang;
import edivad.fluidsystem.datagen.LootTables;
import edivad.fluidsystem.datagen.Recipes;
import edivad.fluidsystem.datagen.TagsProvider;
import edivad.fluidsystem.network.PacketHandler;
import edivad.fluidsystem.setup.ClientSetup;
import edivad.fluidsystem.setup.Config;
import edivad.fluidsystem.setup.FluidsystemCreativeModeTabs;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(FluidSystem.ID)
public class FluidSystem {

  public static final String ID = "fluidsystem";
  public static final String NAME = "FluidSystem";
  public static final Logger LOGGER = LogUtils.getLogger();

  public FluidSystem() {
    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    Registration.init(modEventBus);
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleClientSetup);
    modEventBus.addListener(this::handleGatherData);
    FluidsystemCreativeModeTabs.register(modEventBus);
    Config.init();

    if (FMLEnvironment.dist == Dist.CLIENT) {
      ClientSetup.init(modEventBus);
    }
  }

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    PacketHandler.init();

    // Register TheOneProbe
    if (ModList.get().isLoaded("theoneprobe")) {
      InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPProvider::new);
    }
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    MinecraftForge.EVENT_BUS.register(new UpdateChecker(ID));
    MenuScreens.register(Registration.CONTROLLER_TANK_BLOCK_CONTAINER.get(),
        ScreenModularTank::new);
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var fileHelper = event.getExistingFileHelper();

    generator.addProvider(event.includeServer(), new Recipes(packOutput));
    generator.addProvider(event.includeServer(),
        (DataProvider.Factory<LootTableProvider>) LootTables::create);
    generator.addProvider(event.includeServer(),
        new TagsProvider(packOutput, lookupProvider, fileHelper));
    generator.addProvider(event.includeServer(), new Lang(packOutput));
    generator.addProvider(event.includeClient(), new BlockStates(packOutput, fileHelper));
    generator.addProvider(event.includeClient(),
        new Items(packOutput, event.getExistingFileHelper()));
  }
}
