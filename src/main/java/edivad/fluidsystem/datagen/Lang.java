package edivad.fluidsystem.datagen;

import edivad.fluidsystem.Main;
import edivad.fluidsystem.setup.Registration;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class Lang extends LanguageProvider {

    public Lang(DataGenerator gen) {
        super(gen, Main.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + Main.MODID + "_tab", Main.MODNAME);

        add(Registration.STRUCTURAL_TANK_BLOCK.get(), "Tank Block");
        add(Registration.CONTROLLER_TANK_BLOCK.get(), "Tank Block Controller");
        add(Registration.INTERFACE_TANK_BLOCK.get(), "Tank Block Interface");
        add(Registration.INPUT_TANK_BLOCK.get(), "Tank Block Input");

        add(Registration.PIPE.get(), "Pipe");
        add(Registration.INPUT_PIPE.get(), "Input Pipe");
        add(Registration.OUTPUT_PIPE.get(), "Output Pipe");
        add(Registration.PIPE_CONTROLLER.get(), "Pipe Controller");

        add(Registration.INFINITE_WATER_SOURCE.get(), "Infinite Water Source");

        add(Translations.TANKS_BLOCK, "Tank's block: ");
        add(Translations.LIQUID_AMOUNT, "%smB/%smB");
        add(Translations.LIQUID_PERCENTAGE, "Full: %s");

        add(Translations.TANK_FORMED, "The structure is correctly built");
        add(Translations.TANK_CONTROLLER_MISSING, "The structure has no controller");
        add(Translations.TANK_EXTRA_CONTROLLER, "The structure contains more than one controller");
        add(Translations.TANK_TOO_BIG, "The structure is too big");
        add(Translations.TANK_MISSING_SPACE, "The structure has no Tank Block");
        add(Translations.TANK_EMPTY, "Empty");
        add(Translations.FLUID_FILTERED, "Fluid filtered: %s");
        add(Translations.FLUID_FILTERED_SET, "Fluid filter set: %s");
        add(Translations.FLUID_FILTERED_REMOVE, "Remove fluid filter");

        add(Translations.TANK_BLOCK_TOOLTIP, "Block that adds capacity to the tank");
        add(Translations.TANK_BLOCK_INTERFACE_TOOLTIP, "Block that allows you to withdraw and insert liquids into the tank");
        add(Translations.TANK_BLOCK_CONTROLLER_TOOLTIP, "Main block that allows you to form the tank");
        add(Translations.TANK_BLOCK_INPUT_TOOLTIP, "Block that allows the insertion of liquids through our pipes");
        add(Translations.INPUT_PIPE_TOOLTIP, "Block that allows you to put liquid in the pipes, with a redstone signal");
        add(Translations.OUTPUT_PIPE_TOOLTIP, "Block that allows you to take the liquid from the pipes");
        add(Translations.PIPE_CONTROLLER, "When a redstone signal is applied disconnect the pipes");
    }

}
