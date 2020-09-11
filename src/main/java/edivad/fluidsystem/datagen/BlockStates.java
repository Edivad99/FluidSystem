package edivad.fluidsystem.datagen;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.datagen.util.CustomBlockStateProvider;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;

public class BlockStates extends CustomBlockStateProvider
{
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, Main.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        //Tank
        ModelFile tankBlock = models().cubeAll("structural_tank_block", modLoc("blocks/tank/structural_tank_block"));
        ModelFile tankBlockController = models().cubeBottomTop("controller_tank_block", modLoc("blocks/tank/controller_tank_block"), modLoc("blocks/tank/structural_tank_block"), modLoc("blocks/tank/structural_tank_block"));
        ModelFile tankBlockInterface = models().cubeAll("interface_tank_block", modLoc("blocks/tank/interface_tank_block"));
        ModelFile inputBlock = orientedBlockModel("input_tank_block", modLoc("blocks/tank/structural_tank_block"), modLoc("blocks/tank/input_tank_block"), modLoc("blocks/tank/structural_tank_block"));
        orientedBlock(Registration.INPUT_TANK_BLOCK.get(), inputBlock);

        //General blocks
        ModelFile infiniteWaterSource = models().cubeAll("infinite_water_source", modLoc("blocks/infinite_water_source"));

        simpleBlock(Registration.STRUCTURAL_TANK_BLOCK.get(), tankBlock);
        simpleBlock(Registration.CONTROLLER_TANK_BLOCK.get(), tankBlockController);
        simpleBlock(Registration.INTERFACE_TANK_BLOCK.get(), tankBlockInterface);

        simpleBlock(Registration.INFINITE_WATER_SOURCE.get(), infiniteWaterSource);

        pipeBuilder(Registration.PIPE.get());

        ModelFile pipe_controller_off = models().cubeAll("pipe_controller_off", modLoc("blocks/pipe_controller_off"));
        ModelFile pipe_controller_on = models().cubeAll("pipe_controller_on", modLoc("blocks/pipe_controller_on"));
        blockPowered(Registration.PIPE_CONTROLLER.get(), pipe_controller_off, pipe_controller_on);

        List<ModelFile> inputPipe = orientedBlockPoweredModel("input_pipe", modLoc("blocks/input_pipe_side_off"), modLoc("blocks/input_pipe_side_on"), modLoc("blocks/input_pipe_front"), modLoc("blocks/input_pipe_back"));
        orientedBlockPowered(Registration.INPUT_PIPE.get(), inputPipe.get(0), inputPipe.get(1));

        ModelFile outputPipe = orientedBlockModel("output_pipe", modLoc("blocks/output_pipe_side"), modLoc("blocks/output_pipe_front"), modLoc("blocks/output_pipe_back"));
        orientedBlock(Registration.OUTPUT_PIPE.get(), outputPipe);
    }
}
