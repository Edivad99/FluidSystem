package edivad.fluidsystem.tools;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.IntValue BLOCK_CAPACITY, NUMBER_OF_MODULES;
    public static ForgeConfigSpec.BooleanValue FINITE_WATER_SOURCE;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        SERVER_BUILDER.comment("General settings").push(CATEGORY_GENERAL);

        BLOCK_CAPACITY = SERVER_BUILDER//
                .comment("Indicates how much liquid each Tank Block can hold")//
                .defineInRange("capacity", 160000, 1000, 300000);

        NUMBER_OF_MODULES = SERVER_BUILDER//
                .comment("Indicates the maximum size of each Tank")//
                .defineInRange("modules", 300, 10, 400);

        FINITE_WATER_SOURCE = SERVER_BUILDER//
                .comment("If set to true, it will not be possible to have infinite water source")//
                .define("enabled", false);

        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
