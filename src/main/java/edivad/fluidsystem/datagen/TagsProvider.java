package edivad.fluidsystem.datagen;

import java.util.concurrent.CompletableFuture;
import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.setup.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TagsProvider extends BlockTagsProvider {

  public TagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, lookupProvider, FluidSystem.ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(Registration.INFINITE_WATER_SOURCE.get())
        .add(Registration.PIPE.get())
        .add(Registration.PIPE_CONTROLLER.get())
        .add(Registration.INPUT_PIPE.get())
        .add(Registration.OUTPUT_PIPE.get())
        .add(Registration.STRUCTURAL_TANK_BLOCK.get())
        .add(Registration.INTERFACE_TANK_BLOCK.get())
        .add(Registration.INPUT_TANK_BLOCK.get())
        .add(Registration.CONTROLLER_TANK_BLOCK.get());
  }
}
