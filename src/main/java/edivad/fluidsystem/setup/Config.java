package edivad.fluidsystem.setup;

import edivad.fluidsystem.FluidSystem;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {

  public static void init() {
    var SERVER_BUILDER = new ForgeConfigSpec.Builder();
    SERVER_BUILDER.comment(FluidSystem.NAME + "'s config");
    Tank.registerServerConfig(SERVER_BUILDER);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
  }

  public static class Tank {

    public static ForgeConfigSpec.IntValue BLOCK_CAPACITY;
    public static ForgeConfigSpec.IntValue NUMBER_OF_MODULES;

    public static void registerServerConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
      SERVER_BUILDER.push("Tank");

      Tank.BLOCK_CAPACITY = SERVER_BUILDER
          .comment("Indicates how much liquid each Tank Block can hold")
          .defineInRange("capacity", 160000, 1000, 300000);

      Tank.NUMBER_OF_MODULES = SERVER_BUILDER
          .comment("Indicates the maximum size of each Tank")
          .defineInRange("modules", 300, 10, 400);

      SERVER_BUILDER.pop();
    }
  }
}
