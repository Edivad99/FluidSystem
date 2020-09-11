package edivad.fluidsystem.compat.top;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.tile.pipe.TileEntityBlockFilterablePipe;
import edivad.fluidsystem.tile.tank.TileEntityBaseTankBlock;
import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import edivad.fluidsystem.tools.Config;
import edivad.fluidsystem.tools.Translations;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.function.Function;

public class TOPProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void>
{
    @Override
    public Void apply(ITheOneProbe probe)
    {
        probe.registerProvider(this);
        FluidElement.ID = probe.registerElementFactory(FluidElement::new);
        return null;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data)
    {
        TileEntity te = world.getTileEntity(data.getPos());

        if(te instanceof TileEntityBaseTankBlock)
        {
            TileEntityBaseTankBlock tankBase = ((TileEntityBaseTankBlock) te).getMaster();
            if(tankBase != null)
            {
                TileEntityControllerTankBlock controller = (TileEntityControllerTankBlock) tankBase;

                probeInfo.horizontal().text(new TranslationTextComponent(Translations.TANKS_BLOCK).appendString(String.format("%d/%d", controller.getNumberOfTanksBlock(), Config.NUMBER_OF_MODULES.get())));
                controller.getFluidCap().ifPresent(h ->
                {
                    probeInfo.element(new FluidElement(h.getFluidInTank(0), controller.getTotalCapacity(), controller));
                });
            }
        }
        else if(te instanceof TileEntityBlockFilterablePipe)
        {
            TileEntityBlockFilterablePipe blockOutputPipe = (TileEntityBlockFilterablePipe) te;
            Fluid filter = blockOutputPipe.getFluidFilter();
            if(!filter.isEquivalentTo(Fluids.EMPTY))
            {
                String fluidName = filter.getAttributes().getDisplayName(null).getString();
                probeInfo.horizontal().text(new TranslationTextComponent(Translations.FLUID_FILTERED, fluidName));
            }
        }
    }

    @Override
    public String getID()
    {
        return Main.MODID + ":default";
    }
}
