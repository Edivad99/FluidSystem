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
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration
{
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Main.MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Main.MODID);

    public static Item.Properties globalProperties = new Item.Properties().group(ModSetup.fluidSystemTab).maxStackSize(64);

    public static void init()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<StructuralTankBlock> STRUCTURAL_TANK_BLOCK = BLOCKS.register("structural_tank_block", StructuralTankBlock::new);
    public static final RegistryObject<Item> STRUCTURAL_TANK_BLOCK_ITEM = ITEMS.register("structural_tank_block", () -> new BlockItem(STRUCTURAL_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<TileEntityType<TileEntityStructuralTankBlock>> STRUCTURAL_TANK_BLOCK_TILE = TILES.register("structural_tank_block", () -> TileEntityType.Builder.create(TileEntityStructuralTankBlock::new, STRUCTURAL_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<InterfaceTankBlock> INTERFACE_TANK_BLOCK = BLOCKS.register("interface_tank_block", InterfaceTankBlock::new);
    public static final RegistryObject<Item> INTERFACE_TANK_BLOCK_ITEM = ITEMS.register("interface_tank_block", () -> new BlockItem(INTERFACE_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<TileEntityType<TileEntityInterfaceTankBlock>> INTERFACE_TANK_BLOCK_TILE = TILES.register("interface_tank_block", () -> TileEntityType.Builder.create(TileEntityInterfaceTankBlock::new, INTERFACE_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<InputTankBlock> INPUT_TANK_BLOCK = BLOCKS.register("input_tank_block", InputTankBlock::new);
    public static final RegistryObject<Item> INPUT_TANK_BLOCK_ITEM = ITEMS.register("input_tank_block", () -> new BlockItem(INPUT_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<TileEntityType<TileEntityInputTankBlock>> INPUT_TANK_BLOCK_TILE = TILES.register("input_tank_block", () -> TileEntityType.Builder.create(TileEntityInputTankBlock::new, INPUT_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<ControllerTankBlock> CONTROLLER_TANK_BLOCK = BLOCKS.register("controller_tank_block", ControllerTankBlock::new);
    public static final RegistryObject<Item> CONTROLLER_TANK_BLOCK_ITEM = ITEMS.register("controller_tank_block", () -> new BlockItem(CONTROLLER_TANK_BLOCK.get(), globalProperties));
    public static final RegistryObject<TileEntityType<TileEntityControllerTankBlock>> CONTROLLER_TANK_BLOCK_TILE = TILES.register("controller_tank_block", () -> TileEntityType.Builder.create(TileEntityControllerTankBlock::new, CONTROLLER_TANK_BLOCK.get()).build(null));
    public static final RegistryObject<ContainerType<ContainerTankBlockController>> CONTROLLER_TANK_BLOCK_CONTAINER = CONTAINERS.register("controller_tank_block", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        TileEntity te = inv.player.getEntityWorld().getTileEntity(pos);
        if(!(te instanceof TileEntityControllerTankBlock))
        {
            Main.logger.error("Wrong type of tile entity (expected TileEntityTankBlockController)!");
            return null;
        }

        TileEntityControllerTankBlock tile = (TileEntityControllerTankBlock) te;
        return new ContainerTankBlockController(windowId, inv.player.inventory, tile);
    }));
    
    public static final RegistryObject<InfiniteWaterSource> INFINITE_WATER_SOURCE = BLOCKS.register("infinite_water_source", InfiniteWaterSource::new);
    public static final RegistryObject<Item> INFINITE_WATER_SOURCE_ITEM = ITEMS.register("infinite_water_source", () -> new BlockItem(INFINITE_WATER_SOURCE.get(), globalProperties));
    public static final RegistryObject<TileEntityType<TileEntityInfinityWaterSource>> INFINITE_WATER_SOURCE_TILE = TILES.register("infinite_water_source", () -> TileEntityType.Builder.create(TileEntityInfinityWaterSource::new, INFINITE_WATER_SOURCE.get()).build(null));


    public static final RegistryObject<BlockPipe> PIPE = BLOCKS.register("pipe", BlockPipe::new);
    public static final RegistryObject<Item> PIPE_ITEM = ITEMS.register("pipe", () -> new BlockItem(PIPE.get(), globalProperties));

    public static final RegistryObject<BlockPipeController> PIPE_CONTROLLER = BLOCKS.register("pipe_controller", BlockPipeController::new);
    public static final RegistryObject<Item> IPE_CONTROLLER_ITEM = ITEMS.register("pipe_controller", () -> new BlockItem(PIPE_CONTROLLER.get(), globalProperties));

    public static final RegistryObject<BlockInputPipe> INPUT_PIPE = BLOCKS.register("input_pipe", BlockInputPipe::new);
    public static final RegistryObject<Item> INPUT_PIPE_ITEM = ITEMS.register("input_pipe", () -> new BlockItem(INPUT_PIPE.get(), globalProperties));
    public static final RegistryObject<TileEntityType<TileEntityBlockInputPipe>> INPUT_PIPE_TILE = TILES.register("input_pipe", () -> TileEntityType.Builder.create(TileEntityBlockInputPipe::new, INPUT_PIPE.get()).build(null));
    
    public static final RegistryObject<BlockOutputPipe> OUTPUT_PIPE = BLOCKS.register("output_pipe", BlockOutputPipe::new);
    public static final RegistryObject<Item> OUTPUT_PIPE_ITEM = ITEMS.register("output_pipe", () -> new BlockItem(OUTPUT_PIPE.get(), globalProperties));
    public static final RegistryObject<TileEntityType<TileEntityBlockOutputPipe>> OUTPUT_PIPE_TILE = TILES.register("output_pipe", () -> TileEntityType.Builder.create(TileEntityBlockOutputPipe::new, OUTPUT_PIPE.get()).build(null));
}