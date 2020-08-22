package edivad.fluidsystem.tile.tank;

import edivad.fluidsystem.container.ContainerTankBlockController;
import edivad.fluidsystem.network.PacketHandler;
import edivad.fluidsystem.network.packet.UpdateTankBlockController;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityControllerTankBlock extends TileEntityBaseTankBlock implements INamedContainerProvider
{

    //Client Side
    public FluidStack clientFluidStack = FluidStack.EMPTY;
    public int tanksBlock;
    public int totalCapacity;
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> item = LazyOptional.of(() -> itemHandler);
    private final FluidTank tank = new FluidTank(blockCapacity());
    private LazyOptional<IFluidHandler> fluid;

    public TileEntityControllerTankBlock()
    {
        super(Registration.CONTROLLER_TANK_BLOCK_TILE.get());
    }

    @Override
    public boolean isMaster()
    {
        return true;
    }

    @Override
    public int blockCapacity()
    {
        return 0;
    }

    //Usare solo nei moduli
    public LazyOptional<IFluidHandler> getFluidCap()
    {
        return fluid;
    }

    @Override
    public void tick()
    {
        super.tick();

        if(!world.isRemote)
        {
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UpdateTankBlockController(getPos(), tank.getFluid(), getNumberOfTanksBlock(), getTotalCapacity()));
            ItemStack input = itemHandler.getStackInSlot(0);
            ItemStack output = itemHandler.getStackInSlot(1);
            if(input.getCount() != 1 || !output.isEmpty())
                return;

            input.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(h ->
            {
                FluidStack checkTypeofLiquid = h.getFluidInTank(0);

                if(!tank.isEmpty())//Il tank contiene del liquido
                {
                    if(checkTypeofLiquid.isEmpty())//Significa che l'item è completamente vuoto
                    {
                        FluidActionResult result = FluidUtil.tryFillContainerAndStow(input, tank, itemHandler, h.getTankCapacity(0), null, true);
                        itemHandler.extractItem(0, 1, false);
                        itemHandler.insertItem(1, result.getResult(), false);
                        markDirty();
                    }
                    else if(checkTypeofLiquid.getAmount() < h.getTankCapacity(0))//Significa che l'item è parzialmente pieno
                    {
                        FluidActionResult result = FluidUtil.tryFillContainerAndStow(input, tank, itemHandler, h.getTankCapacity(0) - checkTypeofLiquid.getAmount(), null, true);
                        itemHandler.extractItem(0, 1, false);
                        itemHandler.insertItem(1, result.getResult(), false);
                        markDirty();
                    }
                    else if(checkTypeofLiquid.getAmount() == h.getTankCapacity(0) && checkTypeofLiquid.isFluidEqual(tank.getFluid()))//L'oggetto è pieno e il tank contiene lo stesso liquido
                    {
                        if(h.getTankCapacity(0) <= tank.getSpace())
                        {
                            FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(input, tank, itemHandler, tank.getSpace(), null, true);
                            itemHandler.extractItem(0, 1, false);
                            itemHandler.insertItem(1, result.getResult(), false);
                            markDirty();
                        }
                    }
                }
                else if(tank.isEmpty())//Il tank è vuoto
                {
                    if(checkTypeofLiquid.getAmount() > 0)//Significa che l'item è parzialmente pieno
                    {
                        FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(input, tank, itemHandler, checkTypeofLiquid.getAmount(), null, true);
                        itemHandler.extractItem(0, 1, false);
                        itemHandler.insertItem(1, result.getResult(), false);
                        markDirty();
                    }
                }
            });
        }
    }

    @Override
    protected void onMasterUpdate()
    {
        int newCapacity = getTotalCapacity();
        int oldCapacity = tank.getCapacity();
        int oldSpace = tank.getSpace();
        tank.setCapacity(newCapacity);
        if(oldCapacity > newCapacity && oldSpace == 0)
        {
            tank.drain(new FluidStack(tank.getFluid(), oldCapacity - newCapacity), FluidAction.EXECUTE);
        }
        if(fluid != null)
            fluid.invalidate();
        fluid = LazyOptional.of(() -> tank);
    }

    public ActionResultType activate(PlayerEntity player, World worldIn, BlockPos pos, Hand handIn)
    {
        TileEntityBaseTankBlock master = getMaster();
        if(master != null)
            NetworkHooks.openGui((ServerPlayerEntity) player, this, getPos());
        else
            player.sendStatusMessage(getStatus().getStatusText().mergeStyle(TextFormatting.DARK_RED), false);
        return ActionResultType.SUCCESS;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        tag = super.write(tag);
        tag.put("inventory", itemHandler.serializeNBT());
        tank.writeToNBT(tag);
        return tag;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag)
    {
        super.read(state, tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        tank.readFromNBT(tag);
    }

    private ItemStackHandler createHandler()
    {
        return new ItemStackHandler(2)
        {

            @Override
            protected void onContentsChanged(int slot)
            {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack)
            {
                return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
            }
        };
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if(cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
            return item.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return new ContainerTankBlockController(id, playerInventory, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }
}
