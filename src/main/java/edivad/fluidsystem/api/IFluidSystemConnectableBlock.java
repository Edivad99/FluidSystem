package edivad.fluidsystem.api;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * Implemented on all blocks that can connect to FluidSystem's pipes
 */
public interface IFluidSystemConnectableBlock
{
    /**
     * Checks if pipe can connect from side
     *
     * @param world The world where the block is placed
     * @param myPos Is the position relative to the current block
     * @param side  Is the side that you want to connect
     * @return true if connection is possible
     */
    boolean canConnectTo(IWorld world, BlockPos myPos, Direction side);

    /**
     * Checks if network connections should pass through this block
     * Blocks that can be part of separate pressure networks should return false
     *
     * @param world The world where the block is placed
     * @param myPos Is the position relative to the current block
     * @return true if network connections should pass through this block
     */
    boolean isEndPoint(IWorld world, BlockPos myPos);

    /**
     * Method that given a Direction checks if the pipe is connected
     *
     * @param world The world where the block is placed
     * @param pos   Is the position relative to the current block
     * @param dir   Is the side that you want to check
     * @return Returns true if the tube is connected in that direction
     */
    boolean checkConnection(World world, BlockPos pos, Direction dir);
}
