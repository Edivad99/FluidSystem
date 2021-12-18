package edivad.fluidsystem.blocks.pipe;

import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PipeBlock extends Block implements SimpleWaterloggedBlock, IFluidSystemConnectableBlock {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final EnumProperty<Straight> STRAIGHT = EnumProperty.create("straight", Straight.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape CENTER_SHAPE = box(3.2, 3.2, 3.2, 12.8, 12.8, 12.8);
    private static final VoxelShape NORTH_SHAPE = box(4, 4, 0, 12, 12, 3.5);
    private static final VoxelShape SOUTH_SHAPE = box(4, 4, 12.5, 12, 12, 16);
    private static final VoxelShape EAST_SHAPE = box(12.5, 4, 4, 16, 12, 12);
    private static final VoxelShape WEST_SHAPE = box(0, 4, 4, 3.5, 12, 12);
    private static final VoxelShape UP_SHAPE = box(4, 12.5, 4, 12, 16, 12);
    private static final VoxelShape DOWN_SHAPE = box(4, 0, 4, 12, 3.5, 12);
    private static final VoxelShape STRAIGHT_X_SHAPE = box(0, 4, 4, 16, 12, 12);
    private static final VoxelShape STRAIGHT_Y_SHAPE = box(4, 4, 0, 12, 12, 16);
    private static final VoxelShape STRAIGHT_Z_SHAPE = box(4, 0, 4, 12, 16, 12);

    public PipeBlock() {
        super(Properties.of(Material.METAL).sound(SoundType.STONE).strength(5.0F));
        this.registerDefaultState(defaultBlockState()//
                .setValue(NORTH, false)//
                .setValue(EAST, false)//
                .setValue(SOUTH, false)//
                .setValue(WEST, false)//
                .setValue(UP, false)//
                .setValue(DOWN, false)//
                .setValue(STRAIGHT, Straight.NONE)//
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, STRAIGHT, WATERLOGGED);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(state.getValue(STRAIGHT).equals(Straight.NONE)) {
            VoxelShape shape = CENTER_SHAPE;

            if(state.getValue(NORTH))
                shape = Shapes.or(shape, NORTH_SHAPE);
            if(state.getValue(SOUTH))
                shape = Shapes.or(shape, SOUTH_SHAPE);
            if(state.getValue(EAST))
                shape = Shapes.or(shape, EAST_SHAPE);
            if(state.getValue(WEST))
                shape = Shapes.or(shape, WEST_SHAPE);
            if(state.getValue(UP))
                shape = Shapes.or(shape, UP_SHAPE);
            if(state.getValue(DOWN))
                shape = Shapes.or(shape, DOWN_SHAPE);
            return shape;
        }
        else {
            if(state.getValue(STRAIGHT).equals(Straight.X))
                return STRAIGHT_X_SHAPE;
            if(state.getValue(STRAIGHT).equals(Straight.Y))
                return STRAIGHT_Y_SHAPE;
            else
                return STRAIGHT_Z_SHAPE;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getCollisionShape(state, level, pos, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return getState(level, currentPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        level.setBlockAndUpdate(pos, getState(level, pos));
    }

    private BlockState getState(LevelAccessor world, BlockPos pos) {
        FluidState fluidstate = world.getFluidState(pos);

        Block north = world.getBlockState(pos.north()).getBlock();
        Block east = world.getBlockState(pos.east()).getBlock();
        Block south = world.getBlockState(pos.south()).getBlock();
        Block west = world.getBlockState(pos.west()).getBlock();
        Block up = world.getBlockState(pos.above()).getBlock();
        Block down = world.getBlockState(pos.below()).getBlock();

        boolean isConnectToNorth = checkBlock(world, pos, north, Direction.NORTH);
        boolean isConnectToEast = checkBlock(world, pos, east, Direction.EAST);
        boolean isConnectToSouth = checkBlock(world, pos, south, Direction.SOUTH);
        boolean isConnectToWest = checkBlock(world, pos, west, Direction.WEST);
        boolean isConnectToUp = checkBlock(world, pos, up, Direction.UP);
        boolean isConnectToDown = checkBlock(world, pos, down, Direction.DOWN);

        boolean x = isConnectToEast && isConnectToWest && !isConnectToNorth && !isConnectToSouth && !isConnectToUp && !isConnectToDown;
        boolean y = isConnectToNorth && isConnectToSouth && !isConnectToEast && !isConnectToWest && !isConnectToUp && !isConnectToDown;
        boolean z = isConnectToUp && isConnectToDown && !isConnectToNorth && !isConnectToSouth && !isConnectToEast && !isConnectToWest;

        BlockState result = defaultBlockState()//
                .setValue(NORTH, isConnectToNorth)//
                .setValue(EAST, isConnectToEast)//
                .setValue(SOUTH, isConnectToSouth)//
                .setValue(WEST, isConnectToWest)//
                .setValue(UP, isConnectToUp)//
                .setValue(DOWN, isConnectToDown)//
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);

        if(x)
            return result.setValue(STRAIGHT, Straight.X);
        else if(y)
            return result.setValue(STRAIGHT, Straight.Y);
        else if(z)
            return result.setValue(STRAIGHT, Straight.Z);
        else
            return result.setValue(STRAIGHT, Straight.NONE);
    }

    private boolean checkBlock(LevelAccessor world, BlockPos pos, Block block, Direction direction) {
        return (block instanceof IFluidSystemConnectableBlock connectableBlock) && (connectableBlock.canConnectTo(world, pos.relative(direction), direction));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidStateIn);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluidIn) {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(level, pos, state, fluidIn);
    }

    @Override
    public boolean canConnectTo(LevelAccessor levelAccessor, BlockPos myPos, Direction side) {
        return true;
    }

    @Override
    public boolean isEndPoint(LevelAccessor levelAccessor, BlockPos pos) {
        return false;
    }

    @Override
    public boolean checkConnection(Level level, BlockPos pos, Direction dir) {
        BlockState currentState = level.getBlockState(pos);
        return switch(dir.getOpposite()) {
            case NORTH -> currentState.getValue(NORTH);
            case EAST -> currentState.getValue(EAST);
            case SOUTH -> currentState.getValue(SOUTH);
            case WEST -> currentState.getValue(WEST);
            case UP -> currentState.getValue(UP);
            case DOWN -> currentState.getValue(DOWN);
        };
    }

    public enum Straight implements StringRepresentable {
        X("x"), Y("y"), Z("z"), NONE("none");

        private final String value;

        Straight(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return this.value;
        }
    }
}
