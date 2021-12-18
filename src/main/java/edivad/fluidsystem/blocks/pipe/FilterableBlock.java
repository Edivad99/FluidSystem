package edivad.fluidsystem.blocks.pipe;

import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import edivad.fluidsystem.blocks.RotableBlock;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class FilterableBlock extends RotableBlock {

    public FilterableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if(level.isClientSide)
            return InteractionResult.SUCCESS;

        ItemStack itemOnHand = player.getItemInHand(handIn);
        if(!itemOnHand.isEmpty() && itemOnHand.getItem() instanceof BucketItem bucket) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if(blockentity instanceof FilterablePipeBlockEntity tilePipeFilterable) {
                if(bucket.getFluid().isSame(Fluids.EMPTY)) {
                    if(!tilePipeFilterable.getFluidFilter().isSame(Fluids.EMPTY))
                        player.displayClientMessage(new TranslatableComponent(Translations.FLUID_FILTERED_REMOVE), false);
                }
                else {
                    String fluidName = bucket.getFluid().getAttributes().getDisplayName(null).getString();
                    player.displayClientMessage(new TranslatableComponent(Translations.FLUID_FILTERED_SET, fluidName).withStyle(ChatFormatting.GREEN), false);
                }
                tilePipeFilterable.setFilteredFluid(bucket.getFluid());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
