package edivad.fluidsystem.blockentity.tank;

import edivad.fluidsystem.setup.Config;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class BaseTankBlockEntity extends BlockEntity {

    private BaseTankBlockEntity master;
    private boolean firstRun = true;
    private int size;
    private int totalCapacity;
    private Status status;

    protected BaseTankBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    public abstract boolean isMaster();

    public abstract int blockCapacity();

    public BaseTankBlockEntity getMaster() {
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

    private void setMaster(BaseTankBlockEntity master, int totalCapacity, int size, Status status) {
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
            List<BaseTankBlockEntity> connectedStorages = new ArrayList<>();
            Stack<BaseTankBlockEntity> traversingStorages = new Stack<>();
            BaseTankBlockEntity master = null;
            traversingStorages.add(this);
            while(!traversingStorages.isEmpty()) {
                BaseTankBlockEntity storage = traversingStorages.pop();
                if(storage.isMaster())
                    master = storage;
                connectedStorages.add(storage);
                for(Direction side : Direction.values()) {
                    BlockEntity te = level.getBlockEntity(storage.getBlockPos().relative(side));
                    if(te instanceof BaseTankBlockEntity) {
                        if(!connectedStorages.contains(te) && !traversingStorages.contains(te)) {
                            traversingStorages.add((BaseTankBlockEntity) te);
                        }
                    }
                }
            }

            Status currentStatus = Status.FORMED;
            int controller = 0;
            int calculateCapacity = 0;
            for(BaseTankBlockEntity storage : connectedStorages) {
                if(storage.isMaster())
                    controller++;
                calculateCapacity += storage.blockCapacity();
            }

            if(controller == 0)
                currentStatus = Status.CONTROLLER_MISSING;
            else if(controller > 1)
                currentStatus = Status.EXTRA_CONTROLLER;
            else if(connectedStorages.size() > Config.Tank.NUMBER_OF_MODULES.get())
                currentStatus = Status.TOO_BIG;
            else if(calculateCapacity == 0)
                currentStatus = Status.MISSING_SPACE;

            if(currentStatus != Status.FORMED)
                master = null;

            for(BaseTankBlockEntity storage : connectedStorages) {
                storage.setMaster(master, calculateCapacity, connectedStorages.size(), currentStatus);
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BaseTankBlockEntity blockTile) {
        blockTile.onServerTick(level, pos, state, blockTile);
    }

    public void onServerTick(Level level, BlockPos pos, BlockState state, BaseTankBlockEntity blockTile) {
        if(firstRun) {
            initializeMultiblockIfNecessary();
            firstRun = false;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for(Direction side : Direction.values()) {
            var pos = worldPosition.relative(side);
            if (level != null && level.isLoaded(pos)) {
                var be = level.getBlockEntity(pos);
                if(be instanceof BaseTankBlockEntity baseTankBlock) {
                    baseTankBlock.master = null;
                    baseTankBlock.initializeMultiblockIfNecessary();
                }
            }
        }
    }

    public void onBlockPlacedBy(Player player, Level level, BlockPos pos) {
        if(getMaster() == null)
            player.displayClientMessage(getStatus().getStatusText().withStyle(ChatFormatting.RED), true);
    }

    protected enum Status {
        FORMED, CONTROLLER_MISSING, EXTRA_CONTROLLER, TOO_BIG, MISSING_SPACE;

        public MutableComponent getStatusText() {
            String translatableKey = switch(this) {
                case FORMED -> Translations.TANK_FORMED;
                case CONTROLLER_MISSING -> Translations.TANK_CONTROLLER_MISSING;
                case EXTRA_CONTROLLER -> Translations.TANK_EXTRA_CONTROLLER;
                case TOO_BIG -> Translations.TANK_TOO_BIG;
                case MISSING_SPACE -> Translations.TANK_MISSING_SPACE;
            };
            return Component.translatable(translatableKey);
        }
    }
}
