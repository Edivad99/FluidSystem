package edivad.fluidsystem.blocks.pipe;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import edivad.fluidsystem.blockentity.pipe.InputPipeBlockEntity;
import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class InputPipeBlock extends FilterableBlock implements IFluidSystemConnectableBlock,
    EntityBlock {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public InputPipeBlock() {
    super();
    this.registerDefaultState(defaultBlockState().setValue(POWERED, false));
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new InputPipeBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
      BlockEntityType<T> blockEntityType) {
    return level.isClientSide()
        ? null
        : BaseEntityBlock.createTickerHelper(blockEntityType,
            Registration.INPUT_PIPE_BLOCK_ENTITY.get(), InputPipeBlockEntity::serverTick);
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(POWERED);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    Level level = context.getLevel();
    BlockPos blockpos = context.getClickedPos();
    boolean isBlockPowered = level.hasNeighborSignal(blockpos);
    return super.getStateForPlacement(context).setValue(POWERED, isBlockPowered);
  }

  @Override
  public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn,
      BlockPos fromPos, boolean isMoving) {
    if (!level.isClientSide) {
      boolean isBlockPowered = level.hasNeighborSignal(pos);
      if (state.getValue(POWERED) != isBlockPowered) {
        level.setBlock(pos, state.setValue(POWERED, isBlockPowered), 2);
      }
    }
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flagIn) {
    tooltip.add(
        Component.translatable(Translations.INPUT_PIPE_TOOLTIP).withStyle(ChatFormatting.GRAY));
  }

  @Override
  public boolean canConnectTo(LevelAccessor levelAccessor, BlockPos myPos, Direction side) {
    BlockState state = levelAccessor.getBlockState(myPos);
    return state.getValue(FACING).compareTo(side.getOpposite()) == 0;
  }

  @Override
  public boolean isEndPoint(LevelAccessor levelAccessor, BlockPos pos) {
    return true;
  }

  @Override
  public boolean checkConnection(Level level, BlockPos pos, Direction dir) {
    return canConnectTo(level, pos, dir);
  }
}
