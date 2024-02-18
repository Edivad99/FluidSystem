package edivad.fluidsystem.container;

import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerTankBlockController extends AbstractContainerMenu {

  public ControllerTankBlockEntity blockentity;

  public ContainerTankBlockController(int id, Inventory playerInventory,
      ControllerTankBlockEntity blockentity) {
    super(Registration.CONTROLLER_TANK_BLOCK_MENU.get(), id);
    this.blockentity = blockentity;
    addCustomSlots();
    addPlayerSlots(playerInventory);
  }

  private void addPlayerSlots(Container playerInventory) {
    // Main Inventory
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 9; x++) {
        this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 105 + y * 18));
      }
    }
    // Hotbar
    for (int x = 0; x < 9; x++) {
      this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 163));
    }
  }

  private void addCustomSlots() {
    if (blockentity != null) {
      var itemCap = blockentity.getItemCap(null);
      if (itemCap != null) {
        this.addSlot(new SlotItemHandler(itemCap, 0, 92, 22));
        this.addSlot(new SlotItemHandler(itemCap, 1, 139, 22));
      }
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(
        ContainerLevelAccess.create(blockentity.getLevel(), blockentity.getBlockPos()), player,
        blockentity.getBlockState().getBlock());
  }

  @Override
  public ItemStack quickMoveStack(Player player, int position) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(position);

    if (slot != null && slot.hasItem()) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();

      // tank to inventory
      if (position < 2) {
        if (!this.moveItemStackTo(itemstack1, 2, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      }
      // inventory to tank
      else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.getCount() == 0) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }
    return itemstack;
  }
}
