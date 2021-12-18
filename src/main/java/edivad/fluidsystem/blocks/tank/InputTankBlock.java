package edivad.fluidsystem.blocks.tank;

import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import edivad.fluidsystem.blockentity.tank.InputTankBlockEntity;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class InputTankBlock extends BaseRotableBlock implements IFluidSystemConnectableBlock, EntityBlock {

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InputTankBlockEntity(pos, state);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent(Translations.TANK_BLOCK_INPUT_TOOLTIP).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean canConnectTo(LevelAccessor levelAccessor, BlockPos myPos, Direction side) {
        BlockState state = levelAccessor.getBlockState(myPos);
        return state.getValue(FACING).compareTo(side.getOpposite()) == 0;
    }

    @Override
    public boolean isEndPoint(LevelAccessor levelAccessor, BlockPos myPos) {
        return true;
    }

    @Override
    public boolean checkConnection(Level level, BlockPos pos, Direction dir) {
        return canConnectTo(level, pos, dir);
    }
}
