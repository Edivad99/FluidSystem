package edivad.fluidsystem.setup;

import java.util.Collection;
import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.blockentity.InfinityWaterSourceBlockEntity;
import edivad.fluidsystem.blockentity.pipe.InputPipeBlockEntity;
import edivad.fluidsystem.blockentity.pipe.OutputPipeBlockEntity;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.InputTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.InterfaceTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.StructuralTankBlockEntity;
import edivad.fluidsystem.blocks.InfiniteWaterSourceBlock;
import edivad.fluidsystem.blocks.pipe.InputPipeBlock;
import edivad.fluidsystem.blocks.pipe.OutputPipeBlock;
import edivad.fluidsystem.blocks.pipe.PipeBlock;
import edivad.fluidsystem.blocks.pipe.PipeControllerBlock;
import edivad.fluidsystem.blocks.tank.ControllerTankBlock;
import edivad.fluidsystem.blocks.tank.InputTankBlock;
import edivad.fluidsystem.blocks.tank.InterfaceTankBlock;
import edivad.fluidsystem.blocks.tank.StructuralTankBlock;
import edivad.fluidsystem.container.ContainerTankBlockController;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Registration {

  private static final DeferredRegister.Blocks BLOCKS =
      DeferredRegister.createBlocks(FluidSystem.ID);
  public static final DeferredBlock<StructuralTankBlock> STRUCTURAL_TANK_BLOCK =
      BLOCKS.register("structural_tank_block", StructuralTankBlock::new);
  public static final DeferredBlock<InterfaceTankBlock> INTERFACE_TANK_BLOCK =
      BLOCKS.register("interface_tank_block", InterfaceTankBlock::new);
  public static final DeferredBlock<InputTankBlock> INPUT_TANK_BLOCK =
      BLOCKS.register("input_tank_block", InputTankBlock::new);
  public static final DeferredBlock<ControllerTankBlock> CONTROLLER_TANK_BLOCK =
      BLOCKS.register("controller_tank_block", ControllerTankBlock::new);
  public static final DeferredBlock<InfiniteWaterSourceBlock> INFINITE_WATER_SOURCE =
      BLOCKS.register("infinite_water_source", InfiniteWaterSourceBlock::new);
  public static final DeferredBlock<PipeBlock> PIPE = BLOCKS.register("pipe", PipeBlock::new);
  public static final DeferredBlock<PipeControllerBlock> PIPE_CONTROLLER =
      BLOCKS.register("pipe_controller", PipeControllerBlock::new);
  public static final DeferredBlock<InputPipeBlock> INPUT_PIPE =
      BLOCKS.register("input_pipe", InputPipeBlock::new);
  public static final DeferredBlock<OutputPipeBlock> OUTPUT_PIPE =
      BLOCKS.register("output_pipe", OutputPipeBlock::new);
  private static final DeferredRegister.Items ITEMS =
      DeferredRegister.createItems(FluidSystem.ID);
  public static final DeferredItem<BlockItem> STRUCTURAL_TANK_BLOCK_ITEM =
      ITEMS.registerSimpleBlockItem(STRUCTURAL_TANK_BLOCK);
  public static final DeferredItem<BlockItem> INTERFACE_TANK_BLOCK_ITEM =
      ITEMS.registerSimpleBlockItem(INTERFACE_TANK_BLOCK);
  public static final DeferredItem<BlockItem> INPUT_TANK_BLOCK_ITEM =
      ITEMS.registerSimpleBlockItem(INPUT_TANK_BLOCK);
  public static final DeferredItem<BlockItem> CONTROLLER_TANK_BLOCK_ITEM =
      ITEMS.registerSimpleBlockItem(CONTROLLER_TANK_BLOCK);
  public static final DeferredItem<BlockItem> INFINITE_WATER_SOURCE_ITEM =
      ITEMS.registerSimpleBlockItem(INFINITE_WATER_SOURCE);
  public static final DeferredItem<BlockItem> PIPE_ITEM =
      ITEMS.registerSimpleBlockItem(PIPE);
  public static final DeferredItem<BlockItem> PIPE_CONTROLLER_ITEM =
      ITEMS.registerSimpleBlockItem(PIPE_CONTROLLER);
  public static final DeferredItem<BlockItem> INPUT_PIPE_ITEM =
      ITEMS.registerSimpleBlockItem(INPUT_PIPE);
  public static final DeferredItem<BlockItem> OUTPUT_PIPE_ITEM =
      ITEMS.registerSimpleBlockItem(OUTPUT_PIPE);
  private static final DeferredRegister<MenuType<?>> MENU =
      DeferredRegister.create(BuiltInRegistries.MENU, FluidSystem.ID);
  public static final DeferredHolder<MenuType<?>, MenuType<ContainerTankBlockController>> CONTROLLER_TANK_BLOCK_MENU =
      MENU.register("controller_tank_block", () ->
          new MenuType<>((IContainerFactory<ContainerTankBlockController>) (id, inventory, buf) -> {
            var pos = buf.readBlockPos();
            var be = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
            if (!(be instanceof ControllerTankBlockEntity blockentity)) {
              FluidSystem.LOGGER.error("Wrong type of blockentity (expected ControllerTankBlockEntity)!");
              return null;
            }
            return new ContainerTankBlockController(id, inventory.player.getInventory(), blockentity);
          }, FeatureFlags.DEFAULT_FLAGS)
      );

  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY =
      DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, FluidSystem.ID);
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StructuralTankBlockEntity>> STRUCTURAL_TANK_BLOCK_ENTITY =
      BLOCK_ENTITY.register("structural_tank_block", () ->
          BlockEntityType.Builder.of(StructuralTankBlockEntity::new, STRUCTURAL_TANK_BLOCK.get())
              .build(null));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ControllerTankBlockEntity>> CONTROLLER_TANK_BLOCK_ENTITY =
      BLOCK_ENTITY.register("controller_tank_block",
      () -> BlockEntityType.Builder.of(ControllerTankBlockEntity::new, CONTROLLER_TANK_BLOCK.get())
          .build(null));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InterfaceTankBlockEntity>> INTERFACE_TANK_BLOCK_ENTITY =
      BLOCK_ENTITY.register("interface_tank_block",
          () -> BlockEntityType.Builder.of(InterfaceTankBlockEntity::new, INTERFACE_TANK_BLOCK.get())
              .build(null));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfinityWaterSourceBlockEntity>> INFINITE_WATER_SOURCE_BLOCK_ENTITY =
      BLOCK_ENTITY.register("infinite_water_source",
          () -> BlockEntityType.Builder.of(InfinityWaterSourceBlockEntity::new,
          INFINITE_WATER_SOURCE.get()).build(null));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InputTankBlockEntity>> INPUT_TANK_BLOCK_ENTITY =
      BLOCK_ENTITY.register("input_tank_block",
          () -> BlockEntityType.Builder.of(InputTankBlockEntity::new, INPUT_TANK_BLOCK.get())
              .build(null));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InputPipeBlockEntity>> INPUT_PIPE_BLOCK_ENTITY =
      BLOCK_ENTITY.register("input_pipe",
      () -> BlockEntityType.Builder.of(InputPipeBlockEntity::new, INPUT_PIPE.get()).build(null));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OutputPipeBlockEntity>> OUTPUT_PIPE_BLOCK_ENTITY =
      BLOCK_ENTITY.register("output_pipe",
      () -> BlockEntityType.Builder.of(OutputPipeBlockEntity::new, OUTPUT_PIPE.get()).build(null));

  public static Collection<DeferredHolder<Block, ? extends Block>> getBlockEntries() {
    return BLOCKS.getEntries();
  }

  public static void init(IEventBus eventBus) {
    BLOCKS.register(eventBus);
    ITEMS.register(eventBus);
    BLOCK_ENTITY.register(eventBus);
    MENU.register(eventBus);
  }
}
