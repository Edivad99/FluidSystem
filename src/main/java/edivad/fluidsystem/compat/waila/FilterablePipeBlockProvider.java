package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import snownee.jade.api.IServerDataProvider;

public class FilterablePipeBlockProvider implements IServerDataProvider<BlockEntity> {

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if(blockEntity instanceof FilterablePipeBlockEntity output) {
            Fluid fluid = output.getFluidFilter();
            if(!fluid.isSame(Fluids.EMPTY)) {
                compoundTag.putBoolean("isFluidPresent", true);
                compoundTag.putString("fluid", fluid.getFluidType().getDescriptionId());
            }
            else
                compoundTag.putBoolean("isFluidPresent", false);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Main.MODID, "filterable_pipe_block");
    }
}
