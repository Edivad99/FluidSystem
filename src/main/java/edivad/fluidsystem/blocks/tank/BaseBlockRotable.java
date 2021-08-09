package edivad.fluidsystem.blocks.tank;

import edivad.fluidsystem.blocks.BlockRotable;
import edivad.fluidsystem.tile.tank.TileEntityBaseTankBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class BaseBlockRotable extends BlockRotable {

    public BaseBlockRotable() {
        super(Properties.of(Material.METAL).sound(SoundType.STONE).strength(5.0F));
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if(!worldIn.isClientSide) {
            BlockEntity tile = worldIn.getBlockEntity(pos);

            if(tile instanceof TileEntityBaseTankBlock tankBlock) {
                if(placer instanceof Player player) {
                    tankBlock.onBlockPlacedBy(player, worldIn, pos);
                }
            }
        }
    }
}
