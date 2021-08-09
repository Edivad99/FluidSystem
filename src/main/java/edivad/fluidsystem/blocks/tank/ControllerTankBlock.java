package edivad.fluidsystem.blocks.tank;

import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tile.pipe.TileEntityBlockInputPipe;
import edivad.fluidsystem.tile.tank.TileEntityBaseTankBlock;
import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ControllerTankBlock extends BaseBlock implements EntityBlock
{
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityControllerTankBlock(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        BlockEntityTicker<TileEntityBaseTankBlock> serverTicker = TileEntityBaseTankBlock::serverTick;
        if(Registration.CONTROLLER_TANK_BLOCK_TILE.get() == blockEntityType && !level.isClientSide) {
            return (BlockEntityTicker<T>) serverTicker;
        }
        return null;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
    {
        if(worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity tile = worldIn.getBlockEntity(pos);

        if(tile instanceof TileEntityControllerTankBlock controllerTankBlock)
        {
            return controllerTankBlock.activate(player, worldIn, pos, handIn);
        }
        return InteractionResult.FAIL;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        tooltip.add(new TranslatableComponent(Translations.TANK_BLOCK_CONTROLLER_TOOLTIP).withStyle(ChatFormatting.GRAY));
    }
}
