package edivad.fluidsystem.datagen;

import edivad.fluidsystem.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(Registration.STRUCTURAL_TANK_BLOCK.get(), 4)
                .pattern("aba")
                .pattern("bcb")
                .pattern("aba")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.GREEN_DYE)
                .define('c', Items.HONEYCOMB)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.PIPE.get(), 8)
                .pattern("aaa")
                .pattern("bbb")
                .pattern("aaa")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.BAMBOO)
                .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.PIPE_CONTROLLER.get())
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', Registration.PIPE.get())
                .define('b', Items.REDSTONE_BLOCK)
                .unlockedBy(getHasName(Registration.PIPE.get()), has(Registration.PIPE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.INTERFACE_TANK_BLOCK.get())
                .pattern("   ")
                .pattern("abc")
                .pattern("   ")
                .define('a', Items.BUCKET)
                .define('b', Registration.STRUCTURAL_TANK_BLOCK.get())
                .define('c', Items.WATER_BUCKET)
                .unlockedBy(getHasName(Registration.STRUCTURAL_TANK_BLOCK.get()), has(Registration.STRUCTURAL_TANK_BLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.OUTPUT_PIPE.get())
                .pattern("a")
                .pattern("b")
                .define('a', Registration.STRUCTURAL_TANK_BLOCK.get())
                .define('b', Items.WATER_BUCKET)
                .unlockedBy(getHasName(Registration.STRUCTURAL_TANK_BLOCK.get()), has(Registration.STRUCTURAL_TANK_BLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.INPUT_PIPE.get())
                .pattern("a")
                .pattern("b")
                .define('a', Items.BUCKET)
                .define('b', Registration.STRUCTURAL_TANK_BLOCK.get())
                .unlockedBy(getHasName(Registration.STRUCTURAL_TANK_BLOCK.get()), has(Registration.STRUCTURAL_TANK_BLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.CONTROLLER_TANK_BLOCK.get())
                .pattern("a a")
                .pattern("cbc")
                .pattern("a a")
                .define('a', Items.REDSTONE)
                .define('b', Registration.STRUCTURAL_TANK_BLOCK.get())
                .define('c', Items.COMPARATOR)
                .unlockedBy(getHasName(Registration.STRUCTURAL_TANK_BLOCK.get()), has(Registration.STRUCTURAL_TANK_BLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.INPUT_TANK_BLOCK.get())
                .pattern(" b ")
                .pattern("aaa")
                .pattern(" c ")
                .define('a', Registration.STRUCTURAL_TANK_BLOCK.get())
                .define('b', Items.BUCKET).define('c', Items.REDSTONE)
                .unlockedBy(getHasName(Registration.STRUCTURAL_TANK_BLOCK.get()), has(Registration.STRUCTURAL_TANK_BLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.INFINITE_WATER_SOURCE.get())
                .pattern("aca")
                .pattern("cbc")
                .pattern("aca")
                .define('a', Items.NETHERITE_INGOT)
                .define('b', Items.NETHER_STAR)
                .define('c', Items.BUCKET)
                .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                .save(consumer);
    }
}
