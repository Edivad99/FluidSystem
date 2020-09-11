package edivad.fluidsystem.setup;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.compat.MainCompatHandler;
import edivad.fluidsystem.network.PacketHandler;
import edivad.fluidsystem.tools.Config;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup
{
    public static final ItemGroup fluidSystemTab = new ItemGroup(Main.MODID + "_tab")
    {

        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Registration.INFINITE_WATER_SOURCE.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event)
    {
        PacketHandler.init();

        //Compat
        MainCompatHandler.registerTOP();
    }

    @SubscribeEvent
    public static void handleFiniteWaterSource(BlockEvent.CreateFluidSourceEvent event)
    {
        if(Config.FINITE_WATER_SOURCE.get())
        {
            BlockState state = event.getState();
            FluidState fluidState = state.getFluidState();
            if(fluidState.getFluid().isEquivalentTo(Fluids.WATER))
                event.setResult(Result.DENY);
        }
    }
}
