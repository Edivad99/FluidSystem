package edivad.fluidsystem.blocks.pipe;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import edivad.fluidsystem.blockentity.pipe.OutputPipeBlockEntity;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OutputPipeBlock extends FilterableBlock implements IFluidSystemConnectableBlock,
    EntityBlock {

  public OutputPipeBlock() {
    super();
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new OutputPipeBlockEntity(pos, state);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flagIn) {
    tooltip.add(
        Component.translatable(Translations.OUTPUT_PIPE_TOOLTIP).withStyle(ChatFormatting.GRAY));
  }

  @Override
  public boolean canConnectTo(LevelAccessor levelAccessor, BlockPos myPos, Direction side) {
    BlockState state = levelAccessor.getBlockState(myPos);
    return state.getValue(FACING).compareTo(side) == 0;
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
