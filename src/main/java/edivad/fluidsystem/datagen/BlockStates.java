package edivad.fluidsystem.datagen;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;

public class BlockStates extends CustomBlockStateProvider {

    public BlockStates(PackOutput packOutput, ExistingFileHelper exFileHelper) {
        super(packOutput, Main.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //Tank
        ModelFile tankBlock = models().cubeAll("structural_tank_block", modLoc("block/tank/structural_tank_block"));
        ModelFile tankBlockController = models().cubeBottomTop("controller_tank_block", modLoc("block/tank/controller_tank_block"), modLoc("block/tank/structural_tank_block"), modLoc("block/tank/structural_tank_block"));
        ModelFile tankBlockInterface = models().cubeAll("interface_tank_block", modLoc("block/tank/interface_tank_block"));
        ModelFile inputBlock = orientedBlockModel("input_tank_block", modLoc("block/tank/structural_tank_block"), modLoc("block/tank/input_tank_block"), modLoc("block/tank/structural_tank_block"));
        orientedBlock(Registration.INPUT_TANK_BLOCK.get(), inputBlock);

        //General blocks
        ModelFile infiniteWaterSource = models().cubeAll("infinite_water_source", modLoc("block/infinite_water_source"));

        simpleBlock(Registration.STRUCTURAL_TANK_BLOCK.get(), tankBlock);
        simpleBlock(Registration.CONTROLLER_TANK_BLOCK.get(), tankBlockController);
        simpleBlock(Registration.INTERFACE_TANK_BLOCK.get(), tankBlockInterface);

        simpleBlock(Registration.INFINITE_WATER_SOURCE.get(), infiniteWaterSource);

        pipeBuilder(Registration.PIPE.get());

        ModelFile pipe_controller_off = models().cubeAll("pipe_controller_off", modLoc("block/pipe_controller_off"));
        ModelFile pipe_controller_on = models().cubeAll("pipe_controller_on", modLoc("block/pipe_controller_on"));
        blockPowered(Registration.PIPE_CONTROLLER.get(), pipe_controller_off, pipe_controller_on);

        List<ModelFile> inputPipe = orientedBlockPoweredModel("input_pipe", modLoc("block/input_pipe_side_off"), modLoc("block/input_pipe_side_on"), modLoc("block/input_pipe_front"), modLoc("block/input_pipe_back"));
        orientedBlockPowered(Registration.INPUT_PIPE.get(), inputPipe.get(0), inputPipe.get(1));

        ModelFile outputPipe = orientedBlockModel("output_pipe", modLoc("block/output_pipe_side"), modLoc("block/output_pipe_front"), modLoc("block/output_pipe_back"));
        orientedBlock(Registration.OUTPUT_PIPE.get(), outputPipe);
    }
}
