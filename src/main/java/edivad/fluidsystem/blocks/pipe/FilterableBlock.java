package edivad.fluidsystem.blocks.pipe;

import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import edivad.fluidsystem.blocks.RotableBlock;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class FilterableBlock extends RotableBlock {

  public FilterableBlock() {
    super(Properties.of()
        .mapColor(MapColor.METAL)
        .sound(SoundType.STONE)
        .strength(5.0F)
        .requiresCorrectToolForDrops());
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
      InteractionHand handIn, BlockHitResult hit) {
    if (level.isClientSide) {
      return InteractionResult.SUCCESS;
    }

    var itemOnHand = player.getItemInHand(handIn);
    if (!itemOnHand.isEmpty() && itemOnHand.getItem() instanceof BucketItem bucket) {
      var blockentity = level.getBlockEntity(pos);
      if (blockentity instanceof FilterablePipeBlockEntity tilePipeFilterable) {
        if (bucket.getFluid().isSame(Fluids.EMPTY)) {
          if (!tilePipeFilterable.getFluidFilter().isSame(Fluids.EMPTY)) {
            player.displayClientMessage(Component.translatable(Translations.FLUID_FILTERED_REMOVE),
                true);
          }
        } else {
          var fluidType = bucket.getFluid().getFluidType();
          String fluidName = Component.translatable(fluidType.getDescriptionId()).getString();
          player.displayClientMessage(
              Component.translatable(Translations.FLUID_FILTERED_SET, fluidName)
                  .withStyle(ChatFormatting.GREEN), true);
        }
        tilePipeFilterable.setFilteredFluid(bucket.getFluid());
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.FAIL;
  }
}
