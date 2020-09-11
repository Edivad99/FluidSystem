package edivad.fluidsystem.tile.tank;

import edivad.fluidsystem.tools.Config;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class TileEntityBaseTankBlock extends TileEntity implements ITickableTileEntity
{
    private TileEntityBaseTankBlock master;
    private boolean firstRun = true;
    private int size;
    private int totalCapacity;
    private Status status;

    protected TileEntityBaseTankBlock(TileEntityType<?> tile)
    {
        super(tile);
    }

    public abstract boolean isMaster();

    public abstract int blockCapacity();

    public TileEntityBaseTankBlock getMaster()
    {
        initializeMultiblockIfNecessary();
        return master;
    }

    public int getNumberOfTanksBlock()
    {
        return size;
    }

    public int getTotalCapacity()
    {
        return totalCapacity;
    }

    public Status getStatus()
    {
        return status;
    }

    private void setMaster(TileEntityBaseTankBlock master, int totalCapacity, int size, Status status)
    {
        this.master = master;
        this.totalCapacity = totalCapacity;
        this.size = size;
        this.status = status;
        onMasterUpdate();
    }

    protected void onMasterUpdate()
    {
    }

    private void initializeMultiblockIfNecessary()
    {
        if(master == null || master.isRemoved())
        {
            List<TileEntityBaseTankBlock> connectedStorages = new ArrayList<TileEntityBaseTankBlock>();
            Stack<TileEntityBaseTankBlock> traversingStorages = new Stack<TileEntityBaseTankBlock>();
            TileEntityBaseTankBlock master = null;
            traversingStorages.add(this);
            while(!traversingStorages.isEmpty())
            {
                TileEntityBaseTankBlock storage = traversingStorages.pop();
                if(storage.isMaster())
                    master = storage;
                connectedStorages.add(storage);
                for(Direction side : Direction.values())
                {
                    TileEntity te = world.getTileEntity(storage.getPos().offset(side));
                    if(te instanceof TileEntityBaseTankBlock)
                    {
                        if(!connectedStorages.contains(te) && !traversingStorages.contains(te))
                        {
                            traversingStorages.add((TileEntityBaseTankBlock) te);
                        }
                    }
                }
            }

            Status currentStatus = Status.FORMED;
            int controller = 0;
            int calculateCapacity = 0;
            for(TileEntityBaseTankBlock storage : connectedStorages)
            {
                if(storage.isMaster())
                    controller++;
                calculateCapacity += storage.blockCapacity();
            }

            if(controller == 0)
                currentStatus = Status.CONTROLLER_MISSING;
            else if(controller > 1)
                currentStatus = Status.EXTRA_CONTROLLER;
            else if(connectedStorages.size() > Config.NUMBER_OF_MODULES.get())
                currentStatus = Status.TOO_BIG;

            for(TileEntityBaseTankBlock storage : connectedStorages)
            {
                storage.setMaster(currentStatus == Status.FORMED ? master : null, calculateCapacity, connectedStorages.size(), currentStatus);
            }
        }
    }

    @Override
    public void tick()
    {
        if(!world.isRemote && firstRun)
        {
            initializeMultiblockIfNecessary();
            firstRun = false;
        }
    }

    @Override
    public void remove()
    {
        super.remove();
        for(Direction side : Direction.values())
        {
            TileEntity te = world.getTileEntity(pos.offset(side));
            if(te instanceof TileEntityBaseTankBlock)
            {
                ((TileEntityBaseTankBlock) te).master = null;
                ((TileEntityBaseTankBlock) te).initializeMultiblockIfNecessary();
            }
        }
    }

    public void onBlockPlacedBy(PlayerEntity player, World worldIn, BlockPos pos)
    {
        if(getMaster() == null)
            player.sendStatusMessage(getStatus().getStatusText().mergeStyle(TextFormatting.RED), false);
    }

    protected enum Status
    {
        FORMED,
        CONTROLLER_MISSING,
        EXTRA_CONTROLLER,
        TOO_BIG;

        public TranslationTextComponent getStatusText()
        {
            switch (this)
            {
                case FORMED:
                    return new TranslationTextComponent(Translations.TANK_FORMED);
                case CONTROLLER_MISSING:
                    return new TranslationTextComponent(Translations.TANK_CONTROLLER_MISSING);
                case EXTRA_CONTROLLER:
                    return new TranslationTextComponent(Translations.TANK_EXTRA_CONTROLLER);
                case TOO_BIG:
                    return new TranslationTextComponent(Translations.TANK_TOO_BIG);
                default:
                    return null;
            }
        }
    }
}
