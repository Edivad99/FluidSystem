package edivad.fluidsystem.datagen.util;

import edivad.fluidsystem.blocks.pipe.PipeBlock;
import edivad.fluidsystem.blocks.pipe.PipeBlock.Straight;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.FaceRotation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomBlockStateProvider extends BlockStateProvider {

    private final ExistingFileHelper exFileHelper;
    private final String modid;

    public CustomBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
        this.exFileHelper = exFileHelper;
        this.modid = modid;
    }

    private ModelFile pipeStraight(String modelName, ResourceLocation pipe) {
        BlockModelBuilder model = models().getBuilder(ModelProvider.BLOCK_FOLDER + "/" + modelName).parent(models().getExistingFile(mcLoc("block")));

        model.element()//
                .from(4, 0, 4)//
                .to(12, 16, 12)//
                .face(Direction.NORTH).uvs(0, 2, 16, 14).texture(modid + ":pipe").end()//
                .face(Direction.SOUTH).uvs(0, 2, 16, 14).texture(modid + ":pipe").end()//
                .face(Direction.WEST).uvs(0, 2, 16, 14).texture(modid + ":pipe").end()//
                .face(Direction.EAST).uvs(0, 2, 16, 14).texture(modid + ":pipe").end()//
                .end()//
                .texture("particle", pipe)//
                .texture(modid + ":pipe", pipe);

        return model;
    }

    private ModelFile pipeCenter(String modelName, ResourceLocation pipe) {
        BlockModelBuilder model = models().getBuilder(ModelProvider.BLOCK_FOLDER + "/" + modelName).parent(models().getExistingFile(mcLoc("block")));

        model.element()//
                .from(3.2F, 3.2F, 3.2F)//
                .to(12.8F, 12.8F, 12.8F)//
                .face(Direction.DOWN).uvs(0, 0, 16, 16).texture(modid + ":pipe").end()//
                .face(Direction.UP).uvs(0, 0, 16, 16).texture(modid + ":pipe").end()//
                .face(Direction.NORTH).uvs(0, 0, 16, 16).texture(modid + ":pipe").end()//
                .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture(modid + ":pipe").end()//
                .face(Direction.WEST).uvs(0, 0, 16, 16).texture(modid + ":pipe").end()//
                .face(Direction.EAST).uvs(0, 0, 16, 16).texture(modid + ":pipe").end()//
                .end()//
                .texture("particle", pipe)//
                .texture(modid + ":pipe", pipe);

        return model;
    }

    protected void pipeBuilder(PipeBlock pipe) {

        ModelFile pipeStraight = pipeStraight("pipe_straight", modLoc("blocks/pipe"));
        ModelFile pipeCenter = pipeCenter("pipe_center", modLoc("blocks/pipe"));
        ModelFile pipeUp = new ModelFile.ExistingModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/pipe_up"), this.exFileHelper);
        ModelFile pipeDown = new ModelFile.ExistingModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/pipe_down"), this.exFileHelper);
        ModelFile pipeEast = new ModelFile.ExistingModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/pipe_east"), this.exFileHelper);
        ModelFile pipeNorth = new ModelFile.ExistingModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/pipe_north"), this.exFileHelper);
        ModelFile pipeSouth = new ModelFile.ExistingModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/pipe_south"), this.exFileHelper);
        ModelFile pipeWest = new ModelFile.ExistingModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/pipe_west"), this.exFileHelper);

        MultiPartBlockStateBuilder bld = getMultipartBuilder(pipe);

        //PipeCenter
        bld.part().modelFile(pipeCenter).addModel().condition(PipeBlock.DOWN, false).condition(PipeBlock.EAST, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.SOUTH, false).condition(PipeBlock.UP, false).condition(PipeBlock.WEST, false).condition(PipeBlock.STRAIGHT, Straight.NONE);

        //straight y
        bld.part().modelFile(pipeStraight).rotationX(90).addModel().condition(PipeBlock.DOWN, false).condition(PipeBlock.EAST, false).condition(PipeBlock.NORTH, true).condition(PipeBlock.SOUTH, true).condition(PipeBlock.UP, false).condition(PipeBlock.WEST, false).condition(PipeBlock.STRAIGHT, Straight.Y);
        //straight x
        bld.part().modelFile(pipeStraight).rotationX(90).rotationY(90).addModel().condition(PipeBlock.DOWN, false).condition(PipeBlock.EAST, true).condition(PipeBlock.NORTH, false).condition(PipeBlock.SOUTH, false).condition(PipeBlock.UP, false).condition(PipeBlock.WEST, true).condition(PipeBlock.STRAIGHT, Straight.X);
        //straight z
        bld.part().modelFile(pipeStraight).addModel().condition(PipeBlock.DOWN, true).condition(PipeBlock.EAST, false).condition(PipeBlock.NORTH, false).condition(PipeBlock.SOUTH, false).condition(PipeBlock.UP, true).condition(PipeBlock.WEST, false).condition(PipeBlock.STRAIGHT, Straight.Z);

        //PipeUp
        bld.part().modelFile(pipeUp).addModel().condition(PipeBlock.UP, true).condition(PipeBlock.STRAIGHT, Straight.NONE);
        //PipeDown
        bld.part().modelFile(pipeDown).addModel().condition(PipeBlock.DOWN, true).condition(PipeBlock.STRAIGHT, Straight.NONE);
        //PipeEast
        bld.part().modelFile(pipeEast).rotationX(90).addModel().condition(PipeBlock.EAST, true).condition(PipeBlock.STRAIGHT, Straight.NONE);
        //PipeNorth
        bld.part().modelFile(pipeNorth).addModel().condition(PipeBlock.NORTH, true).condition(PipeBlock.STRAIGHT, Straight.NONE);
        //PipeSouth
        bld.part().modelFile(pipeSouth).addModel().condition(PipeBlock.SOUTH, true).condition(PipeBlock.STRAIGHT, Straight.NONE);
        //PipeWest
        bld.part().modelFile(pipeWest).rotationX(90).addModel().condition(PipeBlock.WEST, true).condition(PipeBlock.STRAIGHT, Straight.NONE);
    }

    protected VariantBlockStateBuilder orientedBlockPowered(Block block, ModelFile modelOff, ModelFile modelOn) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        for(int i = 0; i < 2; i++) {
            builder.partialState()//
                    .with(BlockStateProperties.FACING, Direction.NORTH)//
                    .with(BlockStateProperties.POWERED, i == 0)//
                    .modelForState().modelFile((i == 0) ? modelOn : modelOff)//
                    .addModel();
            builder.partialState()//
                    .with(BlockStateProperties.FACING, Direction.SOUTH)//
                    .with(BlockStateProperties.POWERED, i == 0)//
                    .modelForState()//
                    .modelFile((i == 0) ? modelOn : modelOff)//
                    .rotationY(180)//
                    .addModel();
            builder.partialState()//
                    .with(BlockStateProperties.FACING, Direction.WEST)//
                    .with(BlockStateProperties.POWERED, i == 0)//
                    .modelForState().modelFile((i == 0) ? modelOn : modelOff)//
                    .rotationY(270)//
                    .addModel();
            builder.partialState()//
                    .with(BlockStateProperties.FACING, Direction.EAST)//
                    .with(BlockStateProperties.POWERED, i == 0)//
                    .modelForState()//
                    .modelFile((i == 0) ? modelOn : modelOff)//
                    .rotationY(90)//
                    .addModel();
            builder.partialState()//
                    .with(BlockStateProperties.FACING, Direction.UP)//
                    .with(BlockStateProperties.POWERED, i == 0)//
                    .modelForState()//
                    .modelFile((i == 0) ? modelOn : modelOff)//
                    .rotationX(270)//
                    .addModel();
            builder.partialState()//
                    .with(BlockStateProperties.FACING, Direction.DOWN)//
                    .with(BlockStateProperties.POWERED, i == 0)//
                    .modelForState().modelFile((i == 0) ? modelOn : modelOff)//
                    .rotationX(90)//
                    .addModel();
        }

        return builder;
    }

    protected VariantBlockStateBuilder blockPowered(Block block, ModelFile modelOff, ModelFile modelOn) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        builder.partialState()//
                .with(BlockStateProperties.POWERED, false)//
                .modelForState().modelFile(modelOff)//
                .addModel();

        builder.partialState()//
                .with(BlockStateProperties.POWERED, true)//
                .modelForState().modelFile(modelOn)//
                .addModel();

        return builder;
    }

    protected List<ModelFile> orientedBlockPoweredModel(String modelName, ResourceLocation sideOff, ResourceLocation sideOn, ResourceLocation front, ResourceLocation back) {
        List<ModelFile> models = new ArrayList<>(2);

        for(int i = 0; i < 2; i++) {
            BlockModelBuilder model = models().getBuilder(ModelProvider.BLOCK_FOLDER + "/" + modelName + (i == 0 ? "_off" : "_on")).parent(models().getExistingFile(mcLoc("block")));

            model.element()//
                    .from(0, 0, 0)//
                    .to(16, 16, 16)//
                    .face(Direction.DOWN).cullface(Direction.DOWN).rotation(FaceRotation.UPSIDE_DOWN).uvs(0, 0, 16, 16).texture(modid + ":side").end()//
                    .face(Direction.UP).cullface(Direction.UP).texture(modid + ":side").end()//
                    .face(Direction.NORTH).cullface(Direction.NORTH).texture(modid + ":front").end()//
                    .face(Direction.SOUTH).cullface(Direction.SOUTH).texture(modid + ":back").end()//
                    .face(Direction.WEST).cullface(Direction.WEST).rotation(FaceRotation.COUNTERCLOCKWISE_90).texture(modid + ":side").end()//
                    .face(Direction.EAST).cullface(Direction.EAST).rotation(FaceRotation.CLOCKWISE_90).texture(modid + ":side").end()//
                    .end()//
                    .texture(modid + ":side", i == 0 ? sideOff : sideOn)//
                    .texture(modid + ":front", front)//
                    .texture(modid + ":back", back)//
                    .texture("particle", i == 0 ? sideOff : sideOn);
            models.add(model);
        }
        return models;
    }

    protected VariantBlockStateBuilder orientedBlock(Block block, ModelFile model) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        builder.partialState()//
                .with(BlockStateProperties.FACING, Direction.NORTH)//
                .modelForState().modelFile(model)//
                .addModel();
        builder.partialState()//
                .with(BlockStateProperties.FACING, Direction.SOUTH)//
                .modelForState().modelFile(model)//
                .rotationY(180)//
                .addModel();
        builder.partialState()//
                .with(BlockStateProperties.FACING, Direction.WEST)//
                .modelForState().modelFile(model)//
                .rotationY(270)//
                .addModel();
        builder.partialState()//
                .with(BlockStateProperties.FACING, Direction.EAST)//
                .modelForState().modelFile(model)//
                .rotationY(90)//
                .addModel();
        builder.partialState()//
                .with(BlockStateProperties.FACING, Direction.UP)//
                .modelForState().modelFile(model)//
                .rotationX(270)//
                .addModel();
        builder.partialState()//
                .with(BlockStateProperties.FACING, Direction.DOWN)//
                .modelForState().modelFile(model)//
                .rotationX(90)//
                .addModel();

        return builder;
    }

    protected ModelFile orientedBlockModel(String modelName, ResourceLocation side, ResourceLocation front, ResourceLocation back) {
        BlockModelBuilder model = models().getBuilder(ModelProvider.BLOCK_FOLDER + "/" + modelName).parent(models().getExistingFile(mcLoc("block")));

        model.element()//
                .from(0, 0, 0)//
                .to(16, 16, 16)//
                .face(Direction.DOWN).cullface(Direction.DOWN).rotation(FaceRotation.UPSIDE_DOWN).uvs(0, 0, 16, 16).texture(modid + ":side").end()//
                .face(Direction.UP).cullface(Direction.UP).texture(modid + ":side").end()//
                .face(Direction.NORTH).cullface(Direction.NORTH).texture(modid + ":front").end()//
                .face(Direction.SOUTH).cullface(Direction.SOUTH).texture(modid + ":back").end()//
                .face(Direction.WEST).cullface(Direction.WEST).rotation(FaceRotation.COUNTERCLOCKWISE_90).texture(modid + ":side").end()//
                .face(Direction.EAST).cullface(Direction.EAST).rotation(FaceRotation.CLOCKWISE_90).texture(modid + ":side").end()//
                .end()//
                .texture(modid + ":side", side)//
                .texture(modid + ":front", front)//
                .texture(modid + ":back", back)//
                .texture("particle", side);
        return model;
    }

}
