package edivad.fluidsystem.setup;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blocks.InfiniteWaterSource;
import edivad.fluidsystem.blocks.pipe.BlockInputPipe;
import edivad.fluidsystem.blocks.pipe.BlockOutputPipe;
import edivad.fluidsystem.blocks.pipe.BlockPipe;
import edivad.fluidsystem.blocks.pipe.BlockPipeController;
import edivad.fluidsystem.blocks.tank.ControllerTankBlock;
import edivad.fluidsystem.blocks.tank.InputTankBlock;
import edivad.fluidsystem.blocks.tank.InterfaceTankBlock;
import edivad.fluidsystem.blocks.tank.StructuralTankBlock;
import edivad.fluidsystem.container.ContainerTankBlockController;
import edivad.fluidsystem.tile.pipe.TileEntityBlockInputPipe;
import edivad.fluidsystem.tile.pipe.TileEntityBlockOutputPipe;
import edivad.fluidsystem.tile.TileEntityInfinityWaterSource;
import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import edivad.fluidsystem.tile.tank.TileEntityInputTankBlock;
import edivad.fluidsystem.tile.tank.TileEntityInterfaceTankBlock;
import edivad.fluidsystem.tile.tank.TileEntityStructuralTankBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration
{
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Main.MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Main.MODID);

    public static Item.Properties globalProperties = new Item.Properties().tab(ModSetup.fluidSystemTab).stacksTo(64);

    public static void init()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TILES.register(eventBus);
        CONTAINERS.register(eventBus);
    }
    
    public static final RegistryObject<StructuralTankBlock> STRUCTURAL_TANK_BLOCK = BLOCKS.register("structural_tank_block", StructuralTankBlock::new);
    public static final RegistryObject<Item> STRUCTURAL_TANK_BLOCK_ITEM = ITEMS.register("structural_tank_block", () -> new BlockItem(STRUCTURAL_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<TileEntityStructuralTankBlock>> STRUCTURAL_TANK_BLOCK_TILE = TILES.register("structural_tank_block", () -> BlockEntityType.Builder.of(TileEntityStructuralTankBlock::new, STRUCTURAL_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<InterfaceTankBlock> INTERFACE_TANK_BLOCK = BLOCKS.register("interface_tank_block", InterfaceTankBlock::new);
    public static final RegistryObject<Item> INTERFACE_TANK_BLOCK_ITEM = ITEMS.register("interface_tank_block", () -> new BlockItem(INTERFACE_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<TileEntityInterfaceTankBlock>> INTERFACE_TANK_BLOCK_TILE = TILES.register("interface_tank_block", () -> BlockEntityType.Builder.of(TileEntityInterfaceTankBlock::new, INTERFACE_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<InputTankBlock> INPUT_TANK_BLOCK = BLOCKS.register("input_tank_block", InputTankBlock::new);
    public static final RegistryObject<Item> INPUT_TANK_BLOCK_ITEM = ITEMS.register("input_tank_block", () -> new BlockItem(INPUT_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<TileEntityInputTankBlock>> INPUT_TANK_BLOCK_TILE = TILES.register("input_tank_block", () -> BlockEntityType.Builder.of(TileEntityInputTankBlock::new, INPUT_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<ControllerTankBlock> CONTROLLER_TANK_BLOCK = BLOCKS.register("controller_tank_block", ControllerTankBlock::new);
    public static final RegistryObject<Item> CONTROLLER_TANK_BLOCK_ITEM = ITEMS.register("controller_tank_block", () -> new BlockItem(CONTROLLER_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<TileEntityControllerTankBlock>> CONTROLLER_TANK_BLOCK_TILE = TILES.register("controller_tank_block", () -> BlockEntityType.Builder.of(TileEntityControllerTankBlock::new, CONTROLLER_TANK_BLOCK.get()).build(null));
    public static final RegistryObject<MenuType<ContainerTankBlockController>> CONTROLLER_TANK_BLOCK_CONTAINER = CONTAINERS.register("controller_tank_block", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        BlockEntity te = inv.player.getCommandSenderWorld().getBlockEntity(pos);
        if(!(te instanceof TileEntityControllerTankBlock tile))
        {
            Main.logger.error("Wrong type of tile entity (expected TileEntityTankBlockController)!");
            return null;
        }

        return new ContainerTankBlockController(windowId, inv.player.getInventory(), tile);
    }));
    
    public static final RegistryObject<InfiniteWaterSource> INFINITE_WATER_SOURCE = BLOCKS.register("infinite_water_source", InfiniteWaterSource::new);
    public static final RegistryObject<Item> INFINITE_WATER_SOURCE_ITEM = ITEMS.register("infinite_water_source", () -> new BlockItem(INFINITE_WATER_SOURCE.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<TileEntityInfinityWaterSource>> INFINITE_WATER_SOURCE_TILE = TILES.register("infinite_water_source", () -> BlockEntityType.Builder.of(TileEntityInfinityWaterSource::new, INFINITE_WATER_SOURCE.get()).build(null));


    public static final RegistryObject<BlockPipe> PIPE = BLOCKS.register("pipe", BlockPipe::new);
    public static final RegistryObject<Item> PIPE_ITEM = ITEMS.register("pipe", () -> new BlockItem(PIPE.get(), globalProperties));

    public static final RegistryObject<BlockPipeController> PIPE_CONTROLLER = BLOCKS.register("pipe_controller", BlockPipeController::new);
    public static final RegistryObject<Item> PIPE_CONTROLLER_ITEM = ITEMS.register("pipe_controller", () -> new BlockItem(PIPE_CONTROLLER.get(), globalProperties));

    public static final RegistryObject<BlockInputPipe> INPUT_PIPE = BLOCKS.register("input_pipe", BlockInputPipe::new);
    public static final RegistryObject<Item> INPUT_PIPE_ITEM = ITEMS.register("input_pipe", () -> new BlockItem(INPUT_PIPE.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<TileEntityBlockInputPipe>> INPUT_PIPE_TILE = TILES.register("input_pipe", () -> BlockEntityType.Builder.of(TileEntityBlockInputPipe::new, INPUT_PIPE.get()).build(null));
    
    public static final RegistryObject<BlockOutputPipe> OUTPUT_PIPE = BLOCKS.register("output_pipe", BlockOutputPipe::new);
    public static final RegistryObject<Item> OUTPUT_PIPE_ITEM = ITEMS.register("output_pipe", () -> new BlockItem(OUTPUT_PIPE.get(), globalProperties));
    public static final RegistryObject<BlockEntityType<TileEntityBlockOutputPipe>> OUTPUT_PIPE_TILE = TILES.register("output_pipe", () -> BlockEntityType.Builder.of(TileEntityBlockOutputPipe::new, OUTPUT_PIPE.get()).build(null));
}