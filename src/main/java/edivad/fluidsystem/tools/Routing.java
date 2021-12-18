package edivad.fluidsystem.tools;

import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import edivad.fluidsystem.api.IFluidSystemEject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Routing {

    public static List<IFluidSystemEject> getBlockEject(Level level, BlockPos startPos, BlockPos firstScan) {
        List<IFluidSystemEject> output = new ArrayList<>();
        List<BlockPos> blockVisited = new ArrayList<>();
        Stack<BlockPos> traversingStorages = new Stack<>();
        traversingStorages.add(firstScan);
        blockVisited.add(startPos);

        while(!traversingStorages.isEmpty()) {
            BlockPos posScanBlock = traversingStorages.pop();
            BlockState scanBlockState = level.getBlockState(posScanBlock);
            Block scanBlock = scanBlockState.getBlock();

            if(scanBlock instanceof IFluidSystemConnectableBlock pipe) {
                if(pipe.isEndPoint(level, posScanBlock)) {
                    BlockEntity ScanBlockEntity = level.getBlockEntity(posScanBlock);
                    if(ScanBlockEntity instanceof IFluidSystemEject fluidSystemEject && !output.contains(ScanBlockEntity) && !blockVisited.contains(posScanBlock)) {
                        output.add(fluidSystemEject);
                        blockVisited.add(posScanBlock);
                    }
                }
                else {
                    if(!blockVisited.contains(posScanBlock)) {
                        for(Direction side : Direction.values()) {
                            BlockPos posNewBlock = posScanBlock.relative(side);
                            if(!traversingStorages.contains(posNewBlock) && !blockVisited.contains(posNewBlock)) {
                                BlockState stateNewBlock = level.getBlockState(posNewBlock);
                                Block newBlock = stateNewBlock.getBlock();
                                if(newBlock instanceof IFluidSystemConnectableBlock connectableBlock && connectableBlock.checkConnection(level, posNewBlock, side)) {
                                    traversingStorages.add(posNewBlock);
                                }
                            }
                        }
                        blockVisited.add(posScanBlock);
                    }
                }
            }
        }
        return output;
    }
}
