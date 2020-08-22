package edivad.fluidsystem.datagen;

import edivad.fluidsystem.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider
{

    public Recipes(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shapedRecipe(Registration.STRUCTURAL_TANK_BLOCK.get(), 4)//
                .patternLine("aba")//
                .patternLine("bcb")//
                .patternLine("aba")//
                .key('a', Items.IRON_INGOT)//
                .key('b', Items.GREEN_DYE)//
                .key('c', Items.HONEYCOMB)//
                .addCriterion("iron", hasItem(Items.IRON_INGOT))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.PIPE.get(), 8)//
                .patternLine("aaa")//
                .patternLine("bbb")//
                .patternLine("aaa")//
                .key('a', Items.IRON_INGOT)//
                .key('b', Items.BAMBOO)//
                .addCriterion("bamboo", hasItem(Items.BAMBOO))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.PIPE_CONTROLLER.get())//
                .patternLine(" a ")//
                .patternLine("aba")//
                .patternLine(" a ")//
                .key('a', Registration.PIPE.get())//
                .key('b', Items.REDSTONE_BLOCK)//
                .addCriterion("pipe", hasItem(Registration.PIPE.get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.INTERFACE_TANK_BLOCK.get())//
                .patternLine("   ")//
                .patternLine("abc")//
                .patternLine("   ")//
                .key('a', Items.BUCKET)//
                .key('b', Registration.STRUCTURAL_TANK_BLOCK.get())//
                .key('c', Items.WATER_BUCKET)//
                .addCriterion("structural_tank_block", hasItem(Registration.STRUCTURAL_TANK_BLOCK.get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.OUTPUT_PIPE.get())//
                .patternLine("a")//
                .patternLine("b")//
                .key('a', Registration.STRUCTURAL_TANK_BLOCK.get())//
                .key('b', Items.WATER_BUCKET)//
                .addCriterion("structural_tank_block", hasItem(Registration.STRUCTURAL_TANK_BLOCK.get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.INPUT_PIPE.get())//
                .patternLine("a")//
                .patternLine("b")//
                .key('a', Items.BUCKET)//
                .key('b', Registration.STRUCTURAL_TANK_BLOCK.get())//
                .addCriterion("structural_tank_block", hasItem(Registration.STRUCTURAL_TANK_BLOCK.get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.CONTROLLER_TANK_BLOCK.get())//
                .patternLine("a a")//
                .patternLine("cbc")//
                .patternLine("a a")//
                .key('a', Items.REDSTONE)//
                .key('b', Registration.STRUCTURAL_TANK_BLOCK.get())//
                .key('c', Items.COMPARATOR)//
                .addCriterion("structural_tank_block", hasItem(Registration.STRUCTURAL_TANK_BLOCK.get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.INPUT_TANK_BLOCK.get())//
                .patternLine(" b ")//
                .patternLine("aaa")//
                .patternLine(" c ")//
                .key('a', Registration.STRUCTURAL_TANK_BLOCK.get())//
                .key('b', Items.BUCKET).key('c', Items.REDSTONE)//
                .addCriterion("structural_tank_block", hasItem(Registration.STRUCTURAL_TANK_BLOCK.get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.INFINITE_WATER_SOURCE.get())//
                .patternLine("aca")//
                .patternLine("cbc")//
                .patternLine("aca")//
                .key('a', Items.NETHERITE_INGOT)//
                .key('b', Items.NETHER_STAR)//
                .key('c', Items.BUCKET)//
                .addCriterion("nether_star", hasItem(Items.NETHER_STAR))//
                .build(consumer);
    }
}
