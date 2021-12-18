package edivad.fluidsystem.compat.top;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.pipe.FilterablePipeBlockEntity;
import edivad.fluidsystem.blockentity.tank.BaseTankBlockEntity;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import edivad.fluidsystem.tools.Config;
import edivad.fluidsystem.tools.Translations;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Function;

public class TOPProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe probe) {
        probe.registerProvider(this);
        probe.registerElementFactory(new IElementFactory() {

            @Override
            public IElement createElement(FriendlyByteBuf friendlyByteBuf) {
                return new FluidElement(friendlyByteBuf);
            }

            @Override
            public ResourceLocation getId() {
                return FluidElement.ID;
            }
        });
        return null;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState state, IProbeHitData data) {
        BlockEntity te = level.getBlockEntity(data.getPos());

        if(te instanceof BaseTankBlockEntity tankBlock) {
            BaseTankBlockEntity tankBase = tankBlock.getMaster();
            if(tankBase != null) {
                ControllerTankBlockEntity controller = (ControllerTankBlockEntity) tankBase;

                probeInfo.horizontal().text(new TranslatableComponent(Translations.TANKS_BLOCK).append(String.format("%d/%d", controller.getNumberOfTanksBlock(), Config.NUMBER_OF_MODULES.get())));
                controller.getFluidCap().ifPresent(h -> {
                    probeInfo.element(new FluidElement(h.getFluidInTank(0), controller.getTotalCapacity(), controller));
                });
            }
        }
        else if(te instanceof FilterablePipeBlockEntity) {
            FilterablePipeBlockEntity blockOutputPipe = (FilterablePipeBlockEntity) te;
            Fluid filter = blockOutputPipe.getFluidFilter();
            if(!filter.isSame(Fluids.EMPTY)) {
                String fluidName = filter.getAttributes().getDisplayName(null).getString();
                probeInfo.horizontal().text(new TranslatableComponent(Translations.FLUID_FILTERED, fluidName));
            }
        }
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(Main.MODID, "default");
    }
}
