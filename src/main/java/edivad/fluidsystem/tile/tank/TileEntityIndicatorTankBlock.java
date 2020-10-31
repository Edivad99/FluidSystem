package edivad.fluidsystem.tile.tank;

import edivad.fluidsystem.setup.Registration;

public class TileEntityIndicatorTankBlock extends TileEntityBaseTankBlock
{
    public TileEntityIndicatorTankBlock()
    {
        super(Registration.INDICATOR_TANK_BLOCK_TILE.get());
    }

    @Override
    public boolean isMaster()
    {
        return false;
    }

    @Override
    public int blockCapacity()
    {
        return 0;
    }
}
