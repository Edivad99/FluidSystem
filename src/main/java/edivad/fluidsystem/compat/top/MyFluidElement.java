package edivad.fluidsystem.compat.top;

import org.jetbrains.annotations.NotNull;
import edivad.edivadlib.compat.top.FluidElement;
import edivad.edivadlib.tools.utils.FluidUtils;
import edivad.fluidsystem.FluidSystem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

public class MyFluidElement extends FluidElement {

  public static final ResourceLocation ID = new ResourceLocation(FluidSystem.ID, "fluid_element");

  public MyFluidElement(@NotNull FluidStack fluid, int capacity, BlockEntity blockentity) {
    super(fluid, capacity, FluidUtils.getLiquidColorWithBiome(fluid, blockentity));
  }

  public MyFluidElement(FriendlyByteBuf buf) {
    super(buf.readFluidStack(), buf.readInt(), buf.readInt());
  }

  @Override
  public ResourceLocation getID() {
    return ID;
  }
}
