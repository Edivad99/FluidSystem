package edivad.fluidsystem.datagen;

import edivad.fluidsystem.datagen.util.BaseLootTableProvider;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider
{
    public LootTables(DataGenerator dataGeneratorIn)
    {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables()
    {
        lootTables.put(Registration.STRUCTURAL_TANK_BLOCK.get(), createBaseBlockStandardTable(Registration.STRUCTURAL_TANK_BLOCK.get()));
        lootTables.put(Registration.CONTROLLER_TANK_BLOCK.get(), createBaseBlockStandardTable(Registration.CONTROLLER_TANK_BLOCK.get()));
        lootTables.put(Registration.INDICATOR_TANK_BLOCK.get(), createBaseBlockStandardTable(Registration.INDICATOR_TANK_BLOCK.get()));
        lootTables.put(Registration.INTERFACE_TANK_BLOCK.get(), createBaseBlockStandardTable(Registration.INTERFACE_TANK_BLOCK.get()));
        lootTables.put(Registration.INPUT_TANK_BLOCK.get(), createBaseBlockStandardTable(Registration.INPUT_TANK_BLOCK.get()));
        lootTables.put(Registration.INFINITE_WATER_SOURCE.get(), createBaseBlockStandardTable(Registration.INFINITE_WATER_SOURCE.get()));
        lootTables.put(Registration.PIPE.get(), createBaseBlockStandardTable(Registration.PIPE.get()));
        lootTables.put(Registration.INPUT_PIPE.get(), createBaseBlockStandardTable(Registration.INPUT_PIPE.get()));
        lootTables.put(Registration.OUTPUT_PIPE.get(), createBaseBlockStandardTable(Registration.OUTPUT_PIPE.get()));
        lootTables.put(Registration.PIPE_CONTROLLER.get(), createBaseBlockStandardTable(Registration.PIPE_CONTROLLER.get()));
    }
}
