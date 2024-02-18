package edivad.fluidsystem.blockentity.tank;

import edivad.fluidsystem.container.ContainerTankBlockController;
import edivad.fluidsystem.network.PacketHandler;
import edivad.fluidsystem.network.packet.UpdateControllerTankBlock;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ControllerTankBlockEntity extends BaseTankBlockEntity implements MenuProvider {

  private final ItemStackHandler itemHandler = createHandler();
  private final FluidTank tank = new FluidTank(blockCapacity());

  //Client Side
  public FluidStack clientFluidStack = FluidStack.EMPTY;
  public int tanksBlock;
  public int totalCapacity;

  public ControllerTankBlockEntity(BlockPos pos, BlockState state) {
    super(Registration.CONTROLLER_TANK_BLOCK_ENTITY.get(), pos, state);
  }

  @Override
  public boolean isMaster() {
    return true;
  }

  @Override
  public int blockCapacity() {
    return 0;
  }

  public IFluidHandler getFluidCap() {
    return this.tank;
  }

  public IItemHandler getItemCap(Direction direction) {
    return this.itemHandler;
  }

  @Override
  public void onServerTick(Level level, BlockPos pos, BlockState state,
      BaseTankBlockEntity baseTankBlockEntity) {
    super.onServerTick(level, pos, state, baseTankBlockEntity);
    PacketHandler.sendToAll(
        new UpdateControllerTankBlock(getBlockPos(), tank.getFluid(), getNumberOfTanksBlock(), getTotalCapacity()));
    ItemStack input = itemHandler.getStackInSlot(0);
    ItemStack output = itemHandler.getStackInSlot(1);
    if (input.getCount() != 1 || !output.isEmpty()) {
      return;
    }

    var fluidHandler = input.getCapability(Capabilities.FluidHandler.ITEM);
    if (fluidHandler != null) {
      var checkTypeofLiquid = fluidHandler.getFluidInTank(0);

      if (!tank.isEmpty()) {
        if (checkTypeofLiquid.isEmpty()) {
          var result = FluidUtil.tryFillContainerAndStow(input, tank, itemHandler,
              fluidHandler.getTankCapacity(0), null, true);
          itemHandler.extractItem(0, 1, false);
          itemHandler.insertItem(1, result.getResult(), false);
          setChanged();
        } else if (checkTypeofLiquid.getAmount() < fluidHandler.getTankCapacity(0)) {
          var result = FluidUtil.tryFillContainerAndStow(input, tank, itemHandler,
              fluidHandler.getTankCapacity(0) - checkTypeofLiquid.getAmount(), null, true);
          itemHandler.extractItem(0, 1, false);
          itemHandler.insertItem(1, result.getResult(), false);
          setChanged();
        } else if (checkTypeofLiquid.getAmount() == fluidHandler.getTankCapacity(0) && checkTypeofLiquid.isFluidEqual(tank.getFluid())) {
          if (fluidHandler.getTankCapacity(0) <= tank.getSpace()) {
            var result = FluidUtil.tryEmptyContainerAndStow(input, tank, itemHandler,
                tank.getSpace(), null, true);
            itemHandler.extractItem(0, 1, false);
            itemHandler.insertItem(1, result.getResult(), false);
            setChanged();
          }
        }
      } else {
        if (checkTypeofLiquid.getAmount() > 0) {
          var result = FluidUtil
              .tryEmptyContainerAndStow(input, tank, itemHandler, tank.getSpace(), null, true);
          itemHandler.extractItem(0, 1, false);
          itemHandler.insertItem(1, result.getResult(), false);
          setChanged();
        }
      }
    }
  }

  @Override
  protected void onMasterUpdate() {
    int newCapacity = getTotalCapacity();
    int oldCapacity = tank.getCapacity();
    tank.setCapacity(newCapacity);
    if (oldCapacity > newCapacity && tank.getFluidAmount() > newCapacity) {
      tank.drain(new FluidStack(tank.getFluid(), tank.getFluidAmount() - newCapacity),
          IFluidHandler.FluidAction.EXECUTE);
    }
  }

  public void activate(ServerPlayer player) {
    var master = getMaster();
    if (master != null) {
      player.openMenu(this, getBlockPos());
    } else {
      player.displayClientMessage(getStatus().getStatusText().withStyle(ChatFormatting.RED), true);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("inventory", itemHandler.serializeNBT());
    tank.writeToNBT(tag);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    itemHandler.deserializeNBT(tag.getCompound("inventory"));
    tank.readFromNBT(tag);
  }

  private ItemStackHandler createHandler() {
    return new ItemStackHandler(2) {

      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
      }

      @Override
      public boolean isItemValid(int slot, ItemStack stack) {
        return FluidUtil.getFluidHandler(stack).isPresent();
      }
    };
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
    return new ContainerTankBlockController(id, playerInventory, this);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
  }
}
