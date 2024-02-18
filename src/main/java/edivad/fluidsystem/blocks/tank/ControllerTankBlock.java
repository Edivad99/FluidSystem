package edivad.fluidsystem.blocks.tank;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ControllerTankBlock extends BaseBlock implements EntityBlock {

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new ControllerTankBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
      BlockEntityType<T> blockEntityType) {
    return level.isClientSide()
        ? null
        : BaseEntityBlock.createTickerHelper(blockEntityType,
            Registration.CONTROLLER_TANK_BLOCK_ENTITY.get(), BaseTankBlockEntity::serverTick);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
      InteractionHand handIn, BlockHitResult hit) {
    if (player instanceof ServerPlayer serverPlayer) {
      level.getBlockEntity(pos, Registration.CONTROLLER_TANK_BLOCK_ENTITY.get())
          .ifPresent(blockEntity -> blockEntity.activate(serverPlayer));
    }
    return InteractionResult.sidedSuccess(level.isClientSide());
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
      TooltipFlag flagIn) {
    tooltip.add(Component.translatable(Translations.TANK_BLOCK_CONTROLLER_TOOLTIP)
        .withStyle(ChatFormatting.GRAY));
  }
}
