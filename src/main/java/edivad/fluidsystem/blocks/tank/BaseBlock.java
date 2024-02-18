package edivad.fluidsystem.blocks.tank;

import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class BaseBlock extends Block {

  public BaseBlock() {
    super(Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.STONE)
        .strength(5.0F)
        .requiresCorrectToolForDrops());
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer,
      ItemStack stack) {
    super.setPlacedBy(level, pos, state, placer, stack);
    if (!level.isClientSide) {
      BlockEntity blockentity = level.getBlockEntity(pos);

      if (blockentity instanceof BaseTankBlockEntity tankBlock) {
        if (placer instanceof Player player) {
          tankBlock.onBlockPlacedBy(player, level, pos);
        }
      }
    }
  }
}
