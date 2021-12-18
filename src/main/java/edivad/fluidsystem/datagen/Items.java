package edivad.fluidsystem.datagen;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.blocks.pipe.PipeBlock;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Main.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Tank
        parentedBlock(Registration.STRUCTURAL_TANK_BLOCK.get());
        parentedBlock(Registration.CONTROLLER_TANK_BLOCK.get());
        parentedBlock(Registration.INTERFACE_TANK_BLOCK.get());
        parentedBlock(Registration.INPUT_TANK_BLOCK.get());

        //Pipes
        parentedPipe(Registration.PIPE.get());

        //Other blocks
        parentedBlockActivable(Registration.INPUT_PIPE.get(), false);
        parentedBlock(Registration.OUTPUT_PIPE.get());
        parentedBlock(Registration.INFINITE_WATER_SOURCE.get());
        parentedBlockActivable(Registration.PIPE_CONTROLLER.get(), false);
    }

    private void parentedBlock(Block block) {
        String name = block.getRegistryName().getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)));
    }

    private void parentedBlockActivable(Block block, boolean state) {
        String name = block.getRegistryName().getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name + (state ? "_on" : "_off"))));
    }

    private void parentedPipe(PipeBlock pipe) {
        String name = pipe.getRegistryName().getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name + "_center")));
    }
}
