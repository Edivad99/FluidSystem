package edivad.fluidsystem.blocks.pipe;

import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockPipeController extends Block implements IFluidSystemConnectableBlock
{
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockPipeController()
    {
        super(Properties.of(Material.METAL).sound(SoundType.STONE).strength(5.0F));
        this.registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        boolean isBlockPowered = world.hasNeighborSignal(blockpos);
        return super.getStateForPlacement(context).setValue(POWERED, isBlockPowered);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if(!worldIn.isClientSide)
        {
            boolean isBlockPowered = worldIn.hasNeighborSignal(pos);
            if(state.getValue(POWERED) != isBlockPowered)
            {
                worldIn.setBlock(pos, state.setValue(POWERED, isBlockPowered), 2);
            }
        }
    }

    @Override
    public boolean canConnectTo(LevelAccessor levelAccessor, BlockPos myPos, Direction side)
    {
        return !isEndPoint(levelAccessor, myPos);
    }

    @Override
    public boolean isEndPoint(LevelAccessor levelAccessor, BlockPos myPos)
    {
        BlockState currentState = levelAccessor.getBlockState(myPos);
        return currentState.getValue(POWERED).booleanValue();
    }

    @Override
    public boolean checkConnection(Level level, BlockPos pos, Direction dir)
    {
        return !isEndPoint(level, pos);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        tooltip.add(new TranslatableComponent(Translations.PIPE_CONTROLLER).withStyle(ChatFormatting.GRAY));
    }
}
