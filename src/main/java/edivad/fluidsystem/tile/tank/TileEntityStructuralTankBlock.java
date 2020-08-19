package edivad.fluidsystem.tile.tank;

import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Config;

public class TileEntityStructuralTankBlock extends TileEntityBaseTankBlock
{
    public TileEntityStructuralTankBlock()
    {
        super(Registration.STRUCTURAL_TANK_BLOCK_TILE.get());
    }

    @Override
    public boolean isMaster()
    {
        return false;
    }

    @Override
    public int blockCapacity()
    {
        return Config.BLOCK_CAPACITY.get();
    }
}
