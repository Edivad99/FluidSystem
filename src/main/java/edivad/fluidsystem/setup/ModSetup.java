package edivad.fluidsystem.setup;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.compat.top.TOPProvider;
import edivad.fluidsystem.network.PacketHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final CreativeModeTab fluidSystemTab = new CreativeModeTab(Main.MODID + "_tab") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.INFINITE_WATER_SOURCE.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {
        PacketHandler.init();

        // Register TheOneProbe
        if(ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPProvider::new);
        }
    }

    @SubscribeEvent
    public static void handleFiniteWaterSource(BlockEvent.CreateFluidSourceEvent event) {
        if(Config.General.FINITE_WATER_SOURCE.get()) {
            BlockState state = event.getState();
            FluidState fluidState = state.getFluidState();
            if(fluidState.getType().isSame(Fluids.WATER))
                event.setResult(Result.DENY);
        }
    }
}
