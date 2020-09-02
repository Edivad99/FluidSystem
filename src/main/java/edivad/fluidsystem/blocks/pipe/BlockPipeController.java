package edivad.fluidsystem.blocks.pipe;

import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPipeController extends Block implements IFluidSystemConnectableBlock
{

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockPipeController()
    {
        super(Properties.create(Material.IRON).sound(SoundType.STONE).hardnessAndResistance(5.0F));
        this.setDefaultState(getDefaultState().with(POWERED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        boolean isBlockPowered = world.isBlockPowered(blockpos);
        return super.getStateForPlacement(context).with(POWERED, isBlockPowered);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if(!worldIn.isRemote)
        {
            boolean isBlockPowered = worldIn.isBlockPowered(pos);
            if(state.get(POWERED) != isBlockPowered)
            {
                worldIn.setBlockState(pos, state.with(POWERED, isBlockPowered), 2);
            }
        }
    }

    @Override
    public boolean canConnectTo(IWorld world, BlockPos myPos, Direction side)
    {
        return !isEndPoint(world, myPos);
    }

    @Override
    public boolean isEndPoint(IWorld world, BlockPos myPos)
    {
        BlockState currentState = world.getBlockState(myPos);
        return currentState.get(POWERED).booleanValue();
    }

    @Override
    public boolean checkConnection(World world, BlockPos pos, Direction dir)
    {
        return !isEndPoint(world, pos);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(Translations.PIPE_CONTROLLER).mergeStyle(TextFormatting.GRAY));
    }
}
