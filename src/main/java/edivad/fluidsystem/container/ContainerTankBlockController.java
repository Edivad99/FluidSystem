package edivad.fluidsystem.container;

import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTankBlockController extends Container {

    public TileEntityControllerTankBlock tile;

    public ContainerTankBlockController(int id, PlayerInventory playerInventory, TileEntityControllerTankBlock tile)
    {
        super(Registration.CONTROLLER_TANK_BLOCK_CONTAINER.get(), id);
        this.tile = tile;
        addCustomSlots();
        addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInventory)
    {
        // Main Inventory
        for(int y = 0; y < 3; y++)
            for(int x = 0; x < 9; x++)
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 105 + y * 18));
        // Hotbar
        for(int x = 0; x < 9; x++)
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 163));
    }

    private void addCustomSlots()
    {
        if(tile != null)
        {
            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                this.addSlot(new SlotItemHandler(h, 0, 92, 22));
                this.addSlot(new SlotItemHandler(h, 1, 139, 22));
            });
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerIn, tile.getBlockState().getBlock());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int position)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(position);

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // tank to inventory
            if(position < 2)
            {
                if(!this.mergeItemStack(itemstack1, 2, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            }
            // inventory to tank
            else if(!this.mergeItemStack(itemstack1, 0, 2, false))
                return ItemStack.EMPTY;

            if(itemstack1.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }
        return itemstack;
    }
}
