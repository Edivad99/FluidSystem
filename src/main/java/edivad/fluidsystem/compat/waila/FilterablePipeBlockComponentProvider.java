package edivad.fluidsystem.compat.waila;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class FilterablePipeBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof FilterablePipeBlockEntity) {
            CompoundTag data = accessor.getServerData();
            if(data.getBoolean("isFluidPresent")) {
                String fluidName = Component.translatable(data.getString("fluid")).getString();
                tooltip.add(Component.translatable(Translations.FLUID_FILTERED).append(fluidName));
            }
        }
    }

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
