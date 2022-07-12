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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

public class ControllerTankBlockEntity extends BaseTankBlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> item = LazyOptional.of(() -> itemHandler);
    private final FluidTank tank = new FluidTank(blockCapacity());

    //Client Side
    public FluidStack clientFluidStack = FluidStack.EMPTY;
    public int tanksBlock;
    public int totalCapacity;
    private LazyOptional<IFluidHandler> fluid;

    public ControllerTankBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.CONTROLLER_TANK_BLOCK_TILE.get(), pos, state);
    }

    @Override
    public boolean isMaster() {
        return true;
    }

    @Override
    public int blockCapacity() {
        return 0;
    }

    public LazyOptional<IFluidHandler> getFluidCap() {
        return fluid;
    }

    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state, BaseTankBlockEntity baseTankBlockEntity) {
        super.onServerTick(level, pos, state, baseTankBlockEntity);

        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UpdateControllerTankBlock(getBlockPos(), tank.getFluid(), getNumberOfTanksBlock(), getTotalCapacity()));
        ItemStack input = itemHandler.getStackInSlot(0);
        ItemStack output = itemHandler.getStackInSlot(1);
        if(input.getCount() != 1 || !output.isEmpty())
            return;

        input.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(h -> {
            FluidStack checkTypeofLiquid = h.getFluidInTank(0);

            if(!tank.isEmpty())//Il tank contiene del liquido
            {
                if(checkTypeofLiquid.isEmpty())//Significa che l'item è completamente vuoto
                {
                    FluidActionResult result = FluidUtil.tryFillContainerAndStow(input, tank, itemHandler, h.getTankCapacity(0), null, true);
                    itemHandler.extractItem(0, 1, false);
                    itemHandler.insertItem(1, result.getResult(), false);
                    setChanged();
                }
                else if(checkTypeofLiquid.getAmount() < h.getTankCapacity(0))//Significa che l'item è parzialmente pieno
                {
                    FluidActionResult result = FluidUtil.tryFillContainerAndStow(input, tank, itemHandler, h.getTankCapacity(0) - checkTypeofLiquid.getAmount(), null, true);
                    itemHandler.extractItem(0, 1, false);
                    itemHandler.insertItem(1, result.getResult(), false);
                    setChanged();
                }
                else if(checkTypeofLiquid.getAmount() == h.getTankCapacity(0) && checkTypeofLiquid.isFluidEqual(tank.getFluid()))//L'oggetto è pieno e il tank contiene lo stesso liquido
                {
                    if(h.getTankCapacity(0) <= tank.getSpace()) {
                        FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(input, tank, itemHandler, tank.getSpace(), null, true);
                        itemHandler.extractItem(0, 1, false);
                        itemHandler.insertItem(1, result.getResult(), false);
                        setChanged();
                    }
                }
            }
            else //Il tank è vuoto
            {
                if(checkTypeofLiquid.getAmount() > 0)//Significa che l'item è parzialmente pieno
                {
                    FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(input, tank, itemHandler, tank.getSpace(), null, true);
                    itemHandler.extractItem(0, 1, false);
                    itemHandler.insertItem(1, result.getResult(), false);
                    setChanged();
                }
            }
        });
    }

    @Override
    protected void onMasterUpdate() {
        int newCapacity = getTotalCapacity();
        int oldCapacity = tank.getCapacity();
        tank.setCapacity(newCapacity);
        if(oldCapacity > newCapacity && tank.getFluidAmount() > newCapacity) {
            tank.drain(new FluidStack(tank.getFluid(), tank.getFluidAmount() - newCapacity), FluidAction.EXECUTE);
        }
        if(fluid != null)
            fluid.invalidate();
        fluid = LazyOptional.of(() -> tank);
    }

    public InteractionResult activate(Player player, Level level, BlockPos pos, InteractionHand handIn) {
        BaseTankBlockEntity master = getMaster();
        if(master != null)
            NetworkHooks.openScreen((ServerPlayer) player, this, getBlockPos());
        else
            player.displayClientMessage(getStatus().getStatusText().withStyle(ChatFormatting.DARK_RED), false);
        return InteractionResult.SUCCESS;
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
                return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
            }
        };
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
            return item.cast();
        return super.getCapability(cap, side);
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
