package edivad.fluidsystem.blockentity.pipe;

import edivad.fluidsystem.api.IFluidSystemFilterable;
import edivad.fluidsystem.network.PacketHandler;
import edivad.fluidsystem.network.packet.UpdateFilterablePipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public class FilterablePipeBlockEntity extends BlockEntity implements IFluidSystemFilterable {

  private FluidStack fluidFilter = FluidStack.EMPTY;

  public FilterablePipeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos,
      BlockState state) {
    super(blockEntityType, pos, state);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    fluidFilter.writeToNBT(tag);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    fluidFilter = FluidStack.loadFluidStackFromNBT(tag);
  }

  //Synchronizing on block update
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    CompoundTag tag = new CompoundTag();
    fluidFilter.writeToNBT(tag);
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    CompoundTag tag = pkt.getTag();
    fluidFilter = FluidStack.loadFluidStackFromNBT(tag);
  }

  //Synchronizing on chunk load
  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    fluidFilter.writeToNBT(tag);
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    super.handleUpdateTag(tag);
    fluidFilter = FluidStack.loadFluidStackFromNBT(tag);
  }

  @Override
  public void setFilteredFluid(Fluid fluid) {
    if (fluid.isSame(Fluids.EMPTY)) {
      fluidFilter = FluidStack.EMPTY;
    } else {
      fluidFilter = new FluidStack(fluid, 1000);
    }
    setChanged();
    if (!level.isClientSide) {
      PacketHandler.sendToAll(new UpdateFilterablePipeBlock(getBlockPos(), fluidFilter));
    }
  }

  @Override
  public Fluid getFluidFilter() {
    return fluidFilter.getFluid();
  }
}
