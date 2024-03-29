package edivad.fluidsystem.blocks.tank;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import edivad.fluidsystem.blockentity.tank.InterfaceTankBlockEntity;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InterfaceTankBlock extends BaseBlock implements EntityBlock {

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new InterfaceTankBlockEntity(pos, state);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flagIn) {
    tooltip.add(Component.translatable(Translations.TANK_BLOCK_INTERFACE_TOOLTIP)
        .withStyle(ChatFormatting.GRAY));
  }
}
