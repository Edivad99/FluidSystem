package edivad.fluidsystem.setup;

import edivad.fluidsystem.FluidSystem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FluidsystemCreativeModeTabs {

  private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FluidSystem.ID);

  public static final RegistryObject<CreativeModeTab> FLUIDSYSTEM_TAB =
      CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
          .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
          .title(Component.literal(FluidSystem.NAME))
          .icon(() -> new ItemStack(Registration.INFINITE_WATER_SOURCE_ITEM.get()))
          .displayItems((parameters, output) -> {
            output.accept(new ItemStack(Registration.INFINITE_WATER_SOURCE_ITEM.get()));
            output.accept(new ItemStack(Registration.PIPE_ITEM.get()));
            output.accept(new ItemStack(Registration.PIPE_CONTROLLER_ITEM.get()));
            output.accept(new ItemStack(Registration.INPUT_PIPE_ITEM.get()));
            output.accept(new ItemStack(Registration.OUTPUT_PIPE_ITEM.get()));
            output.accept(new ItemStack(Registration.STRUCTURAL_TANK_BLOCK_ITEM.get()));
            output.accept(new ItemStack(Registration.INTERFACE_TANK_BLOCK_ITEM.get()));
            output.accept(new ItemStack(Registration.INPUT_TANK_BLOCK_ITEM.get()));
            output.accept(new ItemStack(Registration.CONTROLLER_TANK_BLOCK_ITEM.get()));
          }).build());

  public static void register(IEventBus modEventBus) {
    CREATIVE_MODE_TABS.register(modEventBus);
  }
}
