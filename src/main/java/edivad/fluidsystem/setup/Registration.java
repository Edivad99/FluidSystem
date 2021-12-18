package edivad.fluidsystem.setup;

import edivad.fluidsystem.Main;
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
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Main.MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Main.MODID);

    public static Item.Properties globalProperties = new Item.Properties().tab(ModSetup.fluidSystemTab).stacksTo(64);

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TILES.register(eventBus);
        CONTAINERS.register(eventBus);
    }

    public static final RegistryObject<StructuralTankBlock> STRUCTURAL_TANK_BLOCK = BLOCKS.register("structural_tank_block", StructuralTankBlock::new);
    public static final RegistryObject<Item> STRUCTURAL_TANK_BLOCK_ITEM = ITEMS.register("structural_tank_block", () -> new BlockItem(STRUCTURAL_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<StructuralTankBlockEntity>> STRUCTURAL_TANK_BLOCK_TILE = TILES.register("structural_tank_block", () -> BlockEntityType.Builder.of(StructuralTankBlockEntity::new, STRUCTURAL_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<InterfaceTankBlock> INTERFACE_TANK_BLOCK = BLOCKS.register("interface_tank_block", InterfaceTankBlock::new);
    public static final RegistryObject<Item> INTERFACE_TANK_BLOCK_ITEM = ITEMS.register("interface_tank_block", () -> new BlockItem(INTERFACE_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<InterfaceTankBlockEntity>> INTERFACE_TANK_BLOCK_TILE = TILES.register("interface_tank_block", () -> BlockEntityType.Builder.of(InterfaceTankBlockEntity::new, INTERFACE_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<InputTankBlock> INPUT_TANK_BLOCK = BLOCKS.register("input_tank_block", InputTankBlock::new);
    public static final RegistryObject<Item> INPUT_TANK_BLOCK_ITEM = ITEMS.register("input_tank_block", () -> new BlockItem(INPUT_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<InputTankBlockEntity>> INPUT_TANK_BLOCK_TILE = TILES.register("input_tank_block", () -> BlockEntityType.Builder.of(InputTankBlockEntity::new, INPUT_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<ControllerTankBlock> CONTROLLER_TANK_BLOCK = BLOCKS.register("controller_tank_block", ControllerTankBlock::new);
    public static final RegistryObject<Item> CONTROLLER_TANK_BLOCK_ITEM = ITEMS.register("controller_tank_block", () -> new BlockItem(CONTROLLER_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<ControllerTankBlockEntity>> CONTROLLER_TANK_BLOCK_TILE = TILES.register("controller_tank_block", () -> BlockEntityType.Builder.of(ControllerTankBlockEntity::new, CONTROLLER_TANK_BLOCK.get()).build(null));
    public static final RegistryObject<MenuType<ContainerTankBlockController>> CONTROLLER_TANK_BLOCK_CONTAINER = CONTAINERS.register("controller_tank_block", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        BlockEntity be = inv.player.getCommandSenderWorld().getBlockEntity(pos);
        if(!(be instanceof ControllerTankBlockEntity blockentity)) {
            Main.logger.error("Wrong type of blockentity (expected ControllerTankBlockEntity)!");
            return null;
        }

        return new ContainerTankBlockController(windowId, inv.player.getInventory(), blockentity);
    }));

    public static final RegistryObject<InfiniteWaterSourceBlock> INFINITE_WATER_SOURCE = BLOCKS.register("infinite_water_source", InfiniteWaterSourceBlock::new);
    public static final RegistryObject<Item> INFINITE_WATER_SOURCE_ITEM = ITEMS.register("infinite_water_source", () -> new BlockItem(INFINITE_WATER_SOURCE.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<InfinityWaterSourceBlockEntity>> INFINITE_WATER_SOURCE_TILE = TILES.register("infinite_water_source", () -> BlockEntityType.Builder.of(InfinityWaterSourceBlockEntity::new, INFINITE_WATER_SOURCE.get()).build(null));

    public static final RegistryObject<PipeBlock> PIPE = BLOCKS.register("pipe", PipeBlock::new);
    public static final RegistryObject<Item> PIPE_ITEM = ITEMS.register("pipe", () -> new BlockItem(PIPE.get(), globalProperties));

    public static final RegistryObject<PipeControllerBlock> PIPE_CONTROLLER = BLOCKS.register("pipe_controller", PipeControllerBlock::new);
    public static final RegistryObject<Item> PIPE_CONTROLLER_ITEM = ITEMS.register("pipe_controller", () -> new BlockItem(PIPE_CONTROLLER.get(), globalProperties));

    public static final RegistryObject<InputPipeBlock> INPUT_PIPE = BLOCKS.register("input_pipe", InputPipeBlock::new);
    public static final RegistryObject<Item> INPUT_PIPE_ITEM = ITEMS.register("input_pipe", () -> new BlockItem(INPUT_PIPE.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<InputPipeBlockEntity>> INPUT_PIPE_TILE = TILES.register("input_pipe", () -> BlockEntityType.Builder.of(InputPipeBlockEntity::new, INPUT_PIPE.get()).build(null));

    public static final RegistryObject<OutputPipeBlock> OUTPUT_PIPE = BLOCKS.register("output_pipe", OutputPipeBlock::new);
    public static final RegistryObject<Item> OUTPUT_PIPE_ITEM = ITEMS.register("output_pipe", () -> new BlockItem(OUTPUT_PIPE.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<OutputPipeBlockEntity>> OUTPUT_PIPE_TILE = TILES.register("output_pipe", () -> BlockEntityType.Builder.of(OutputPipeBlockEntity::new, OUTPUT_PIPE.get()).build(null));
}