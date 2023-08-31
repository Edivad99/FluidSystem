package edivad.fluidsystem.blocks.pipe;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PipeControllerBlock extends Block implements IFluidSystemConnectableBlock {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public PipeControllerBlock() {
    super(Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.STONE)
        .strength(5.0F)
        .requiresCorrectToolForDrops());
    this.registerDefaultState(defaultBlockState().setValue(POWERED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
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
  public boolean canConnectTo(LevelAccessor levelAccessor, BlockPos myPos, Direction side) {
    return !isEndPoint(levelAccessor, myPos);
  }

  @Override
  public boolean isEndPoint(LevelAccessor levelAccessor, BlockPos myPos) {
    BlockState currentState = levelAccessor.getBlockState(myPos);
    return currentState.getValue(POWERED);
  }

  @Override
  public boolean checkConnection(Level level, BlockPos pos, Direction dir) {
    return !isEndPoint(level, pos);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flagIn) {
    tooltip.add(
        Component.translatable(Translations.PIPE_CONTROLLER).withStyle(ChatFormatting.GRAY));
  }
}
