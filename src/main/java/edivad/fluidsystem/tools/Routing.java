package edivad.fluidsystem.tools;

import edivad.fluidsystem.api.IFluidSystemConnectableBlock;
import edivad.fluidsystem.api.IFluidSystemEject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Routing
{

    public static List<IFluidSystemEject> getBlockEject(World world, BlockPos startPos, BlockPos firstScan)
    {
        List<IFluidSystemEject> output = new ArrayList<>();
        List<BlockPos> blockVisited = new ArrayList<>();
        Stack<BlockPos> traversingStorages = new Stack<>();
        traversingStorages.add(firstScan);
        blockVisited.add(startPos);

        while(!traversingStorages.isEmpty())
        {
            BlockPos posScanBlock = traversingStorages.pop();
            BlockState scanBlockState = world.getBlockState(posScanBlock);
            Block scanBlock = scanBlockState.getBlock();

            if(scanBlock instanceof IFluidSystemConnectableBlock)
            {
                IFluidSystemConnectableBlock pipe = (IFluidSystemConnectableBlock) scanBlock;
                if(pipe.isEndPoint(world, posScanBlock))
                {
                    TileEntity tileScanBlock = world.getTileEntity(posScanBlock);
                    if(tileScanBlock instanceof IFluidSystemEject && !output.contains(tileScanBlock) && !blockVisited.contains(posScanBlock))
                    {
                        output.add((IFluidSystemEject) tileScanBlock);
                        blockVisited.add(posScanBlock);
                    }
                }
                else
                {
                    if(!blockVisited.contains(posScanBlock))
                    {
                        for(Direction side : Direction.values())
                        {
                            BlockPos posNewBlock = posScanBlock.offset(side);
                            if(!traversingStorages.contains(posNewBlock) && !blockVisited.contains(posNewBlock))
                            {
                                BlockState stateNewBlock = world.getBlockState(posNewBlock);
                                Block newBlock = stateNewBlock.getBlock();
                                if(newBlock instanceof IFluidSystemConnectableBlock && ((IFluidSystemConnectableBlock) newBlock).checkConnection(world, posNewBlock, side))
                                {
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
