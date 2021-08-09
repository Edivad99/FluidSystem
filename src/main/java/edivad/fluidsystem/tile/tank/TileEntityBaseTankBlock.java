package edivad.fluidsystem.tile.tank;

import edivad.fluidsystem.tools.Config;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class TileEntityBaseTankBlock extends BlockEntity {

    private TileEntityBaseTankBlock master;
    private boolean firstRun = true;
    private int size;
    private int totalCapacity;
    private Status status;

    protected TileEntityBaseTankBlock(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public abstract boolean isMaster();

    public abstract int blockCapacity();

    public TileEntityBaseTankBlock getMaster() {
        initializeMultiblockIfNecessary();
        return master;
    }

    public int getNumberOfTanksBlock() {
        return size;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public Status getStatus() {
        return status;
    }

    private void setMaster(TileEntityBaseTankBlock master, int totalCapacity, int size, Status status) {
        this.master = master;
        this.totalCapacity = totalCapacity;
        this.size = size;
        this.status = status;
        onMasterUpdate();
    }

    protected void onMasterUpdate() {
    }

    private void initializeMultiblockIfNecessary() {
        if(master == null || master.isRemoved()) {
            List<TileEntityBaseTankBlock> connectedStorages = new ArrayList<>();
            Stack<TileEntityBaseTankBlock> traversingStorages = new Stack<>();
            TileEntityBaseTankBlock master = null;
            traversingStorages.add(this);
            while(!traversingStorages.isEmpty()) {
                TileEntityBaseTankBlock storage = traversingStorages.pop();
                if(storage.isMaster())
                    master = storage;
                connectedStorages.add(storage);
                for(Direction side : Direction.values()) {
                    BlockEntity te = level.getBlockEntity(storage.getBlockPos().relative(side));
                    if(te instanceof TileEntityBaseTankBlock) {
                        if(!connectedStorages.contains(te) && !traversingStorages.contains(te)) {
                            traversingStorages.add((TileEntityBaseTankBlock) te);
                        }
                    }
                }
            }

            Status currentStatus = Status.FORMED;
            int controller = 0;
            int calculateCapacity = 0;
            for(TileEntityBaseTankBlock storage : connectedStorages) {
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
            else if(calculateCapacity == 0)
                currentStatus = Status.MISSING_SPACE;

            if(currentStatus != Status.FORMED)
                master = null;

            for(TileEntityBaseTankBlock storage : connectedStorages) {
                storage.setMaster(master, calculateCapacity, connectedStorages.size(), currentStatus);
            }
        }
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileEntityBaseTankBlock blockTile) {
        blockTile.onServerTick(level, blockPos, blockState, blockTile);
    }

    public void onServerTick(Level level, BlockPos blockPos, BlockState blockState, TileEntityBaseTankBlock blockTile) {
        if(firstRun) {
            initializeMultiblockIfNecessary();
            firstRun = false;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for(Direction side : Direction.values()) {
            BlockEntity te = level.getBlockEntity(worldPosition.relative(side));
            if(te instanceof TileEntityBaseTankBlock baseTankBlock) {
                baseTankBlock.master = null;
                baseTankBlock.initializeMultiblockIfNecessary();
            }
        }
    }

    public void onBlockPlacedBy(Player player, Level worldIn, BlockPos pos) {
        if(getMaster() == null)
            player.displayClientMessage(getStatus().getStatusText().withStyle(ChatFormatting.RED), false);
    }

    protected enum Status {
        FORMED, CONTROLLER_MISSING, EXTRA_CONTROLLER, TOO_BIG, MISSING_SPACE;

        public TranslatableComponent getStatusText() {
            return switch(this) {
                case FORMED -> new TranslatableComponent(Translations.TANK_FORMED);
                case CONTROLLER_MISSING -> new TranslatableComponent(Translations.TANK_CONTROLLER_MISSING);
                case EXTRA_CONTROLLER -> new TranslatableComponent(Translations.TANK_EXTRA_CONTROLLER);
                case TOO_BIG -> new TranslatableComponent(Translations.TANK_TOO_BIG);
                case MISSING_SPACE -> new TranslatableComponent(Translations.TANK_MISSING_SPACE);
                default -> null;
            };
        }
    }
}
