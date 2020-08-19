package edivad.fluidsystem.blocks.pipe;

import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockPipe extends Block implements IWaterLoggable, IFluidSystemConnectableBlock {
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final EnumProperty<Straight> STRAIGHT = EnumProperty.create("straight", Straight.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape CENTER_SHAPE = makeCuboidShape(3.2, 3.2, 3.2, 12.8, 12.8, 12.8);
    private static final VoxelShape NORTH_SHAPE = makeCuboidShape(4, 4, 0, 12, 12, 3.5);
    private static final VoxelShape SOUTH_SHAPE = makeCuboidShape(4, 4, 12.5, 12, 12, 16);
    private static final VoxelShape EAST_SHAPE = makeCuboidShape(12.5, 4, 4, 16, 12, 12);
    private static final VoxelShape WEST_SHAPE = makeCuboidShape(0, 4, 4, 3.5, 12, 12);
    private static final VoxelShape UP_SHAPE = makeCuboidShape(4, 12.5, 4, 12, 16, 12);
    private static final VoxelShape DOWN_SHAPE = makeCuboidShape(4, 0, 4, 12, 3.5, 12);
    private static final VoxelShape STRAIGHT_X_SHAPE = makeCuboidShape(0, 4, 4, 16, 12, 12);
    private static final VoxelShape STRAIGHT_Y_SHAPE = makeCuboidShape(4, 4, 0, 12, 12, 16);
    private static final VoxelShape STRAIGHT_Z_SHAPE = makeCuboidShape(4, 0, 4, 12, 16, 12);

    public BlockPipe()
    {
        super(Properties.create(Material.IRON).sound(SoundType.STONE).hardnessAndResistance(5.0F));
        this.setDefaultState(getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false).with(STRAIGHT, Straight.NONE).with(WATERLOGGED, false));
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, STRAIGHT, WATERLOGGED);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if(state.get(STRAIGHT).equals(Straight.NONE))
        {
            VoxelShape shape = CENTER_SHAPE;

            if(state.get(NORTH).booleanValue())
                shape = VoxelShapes.or(shape, NORTH_SHAPE);
            if(state.get(SOUTH).booleanValue())
                shape = VoxelShapes.or(shape, SOUTH_SHAPE);
            if(state.get(EAST).booleanValue())
                shape = VoxelShapes.or(shape, EAST_SHAPE);
            if(state.get(WEST).booleanValue())
                shape = VoxelShapes.or(shape, WEST_SHAPE);
            if(state.get(UP).booleanValue())
                shape = VoxelShapes.or(shape, UP_SHAPE);
            if(state.get(DOWN).booleanValue())
                shape = VoxelShapes.or(shape, DOWN_SHAPE);
            return shape;
        }
        else
        {
            if(state.get(STRAIGHT).equals(Straight.X))
                return STRAIGHT_X_SHAPE;
            if(state.get(STRAIGHT).equals(Straight.Y))
                return STRAIGHT_Y_SHAPE;
            else
                return STRAIGHT_Z_SHAPE;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getCollisionShape(state, worldIn, pos, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getState(context.getWorld(), context.getPos());
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        return getState(worldIn, currentPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        world.setBlockState(pos, getState(world, pos));
    }

    private BlockState getState(IWorld world, BlockPos pos) {
        FluidState fluidstate = world.getFluidState(pos);

        Block north = world.getBlockState(pos.north()).getBlock();
        Block east = world.getBlockState(pos.east()).getBlock();
        Block south = world.getBlockState(pos.south()).getBlock();
        Block west = world.getBlockState(pos.west()).getBlock();
        Block up = world.getBlockState(pos.up()).getBlock();
        Block down = world.getBlockState(pos.down()).getBlock();

        boolean isConnectToNorth = checkBlock(world, pos, north, Direction.NORTH);
        boolean isConnectToEast = checkBlock(world, pos, east, Direction.EAST);
        boolean isConnectToSouth = checkBlock(world, pos, south, Direction.SOUTH);
        boolean isConnectToWest = checkBlock(world, pos, west, Direction.WEST);
        boolean isConnectToUp = checkBlock(world, pos, up, Direction.UP);
        boolean isConnectToDown = checkBlock(world, pos, down, Direction.DOWN);

        boolean x = isConnectToEast && isConnectToWest && !isConnectToNorth && !isConnectToSouth && !isConnectToUp && !isConnectToDown;
        boolean y = isConnectToNorth && isConnectToSouth && !isConnectToEast && !isConnectToWest && !isConnectToUp && !isConnectToDown;
        boolean z = isConnectToUp && isConnectToDown && !isConnectToNorth && !isConnectToSouth && !isConnectToEast && !isConnectToWest;

        BlockState result =  getDefaultState()
            .with(NORTH, isConnectToNorth)
            .with(EAST, isConnectToEast)
            .with(SOUTH, isConnectToSouth)
            .with(WEST, isConnectToWest)
            .with(UP, isConnectToUp)
            .with(DOWN, isConnectToDown)
            .with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER));

        if(x)
            return result.with(STRAIGHT, Straight.X);
        else if(y)
            return result.with(STRAIGHT, Straight.Y);
        else if(z)
            return result.with(STRAIGHT, Straight.Z);
        else
            return result.with(STRAIGHT, Straight.NONE);
    }

    private boolean checkBlock(IWorld world, BlockPos pos, Block block, Direction direction)
    {
        return (block instanceof IFluidSystemConnectableBlock) && ((IFluidSystemConnectableBlock)block).canConnectTo(world, pos.offset(direction), direction);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn)
    {
        return IWaterLoggable.super.receiveFluid(worldIn, pos, state, fluidStateIn);
    }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn)
    {
        return IWaterLoggable.super.canContainFluid(worldIn, pos, state, fluidIn);
    }

    @Override
    public boolean canConnectTo(IWorld world, BlockPos myPos, Direction side)
    {
        return true;
    }

    @Override
    public boolean isEndPoint(IWorld world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean checkConnection(World world, BlockPos pos, Direction dir)
    {
        BlockState currentState = world.getBlockState(pos);
        switch (dir.getOpposite())
        {
            case NORTH: return currentState.get(NORTH).booleanValue();
            case EAST: return currentState.get(EAST).booleanValue();
            case SOUTH: return currentState.get(SOUTH).booleanValue();
            case WEST: return currentState.get(WEST).booleanValue();
            case UP: return currentState.get(UP).booleanValue();
            case DOWN: return currentState.get(DOWN).booleanValue();
        }
        return false;
    }

    public enum Straight implements IStringSerializable
    {

        X("x"), Y("y"), Z("z"), NONE("none");

        private final String value;

        private Straight(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return this.getString();
        }

        @Override
        public String getString()
        {
            return this.value;
        }
    }
}
