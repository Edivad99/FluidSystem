package edivad.fluidsystem.tools;

import edivad.fluidsystem.FluidSystem;

public class Translations {

  public static final String TANKS_BLOCK = "gui." + FluidSystem.ID + ".tanksBlock";
  public static final String LIQUID_AMOUNT = "gui." + FluidSystem.ID + ".liquidAmount";
  public static final String LIQUID_PERCENTAGE = "gui." + FluidSystem.ID + ".liquidPercentage";

  public static final String TANK_FORMED = "message." + FluidSystem.ID + ".tankFormed";
  public static final String TANK_CONTROLLER_MISSING =
      "message." + FluidSystem.ID + ".tankControllerMissing";
  public static final String TANK_EXTRA_CONTROLLER =
      "message." + FluidSystem.ID + ".tankExtraController";
  public static final String TANK_TOO_BIG = "message." + FluidSystem.ID + ".tankTooBig";
  public static final String TANK_MISSING_SPACE = "message." + FluidSystem.ID + ".tankMissingSpace";
  public static final String FLUID_FILTERED = "message." + FluidSystem.ID + ".fluidFiltered";
  public static final String FLUID_FILTERED_SET = "message." + FluidSystem.ID + ".fluidFilteredSet";
  public static final String FLUID_FILTERED_REMOVE =
      "message." + FluidSystem.ID + ".fluidFilterRemoved";
  public static final String TANK_EMPTY = "message." + FluidSystem.ID + ".tankEmpty";

  public static final String TANK_BLOCK_TOOLTIP = "tooltip." + FluidSystem.ID + ".tankBlock";
  public static final String TANK_BLOCK_INTERFACE_TOOLTIP =
      "tooltip." + FluidSystem.ID + ".tankBlockInterface";
  public static final String TANK_BLOCK_CONTROLLER_TOOLTIP =
      "tooltip." + FluidSystem.ID + ".tankBlockController";
  public static final String TANK_BLOCK_INPUT_TOOLTIP =
      "tooltip." + FluidSystem.ID + ".tankBlockInput";
  public static final String INPUT_PIPE_TOOLTIP = "tooltip." + FluidSystem.ID + ".inputPipe";
  public static final String OUTPUT_PIPE_TOOLTIP = "tooltip." + FluidSystem.ID + ".outputPipe";
  public static final String PIPE_CONTROLLER = "tooltip." + FluidSystem.ID + ".pipeController";

  public static final String JADE_ENABLE_TANK_BLOCK =
      "config.jade.plugin_" + FluidSystem.ID + ".base_tank_block";
  public static final String JADE_ENABLE_FILTERABLE_BLOCK =
      "config.jade.plugin_" + FluidSystem.ID + ".filterable_pipe_block";
}
