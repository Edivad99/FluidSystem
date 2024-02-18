package edivad.fluidsystem.client.screen;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import edivad.edivadlib.tools.utils.FluidUtils;
import edivad.fluidsystem.FluidSystem;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import edivad.fluidsystem.container.ContainerTankBlockController;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

public class ScreenModularTank extends AbstractContainerScreen<ContainerTankBlockController> {

  private static final ResourceLocation TEXTURES = new ResourceLocation(FluidSystem.ID,
      "textures/gui/controller_tank_block.png");
  private static final MutableComponent TANK_EMPTY =
      Component.translatable(Translations.TANK_EMPTY);
  private static final DecimalFormat FORMAT = new DecimalFormat("#,##0");
  private final ControllerTankBlockEntity blockentity;

  public ScreenModularTank(ContainerTankBlockController screenContainer, Inventory inv,
      Component title) {
    super(screenContainer, inv, title);
    this.imageWidth = 176;
    this.imageHeight = 187;
    this.blockentity = screenContainer.blockentity;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    guiGraphics.blit(TEXTURES, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    //Render fluid
    FluidStack fluid = blockentity.clientFluidStack;
    int index = FluidUtils.getFluidScaled(43, fluid, blockentity.totalCapacity);
    if (!fluid.isEmpty()) {
      TextureAtlasSprite fluidTexture = FluidUtils.getFluidTexture(fluid);

      var color = FluidUtils.getLiquidColorWithBiome(fluid, blockentity);
      var red = FluidUtils.getRed(color);
      var green = FluidUtils.getGreen(color);
      var blue = FluidUtils.getBlue(color);
      var alpha = FluidUtils.getAlpha(color);

      guiGraphics.blit(this.leftPos + 8, this.topPos + 19 + index, 176, 52, 43 - index,
          fluidTexture, red, green, blue, alpha);
    }
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    var tanksBlock = Component.translatable(Translations.TANKS_BLOCK, blockentity.tanksBlock);
    guiGraphics
        .drawString(this.font, tanksBlock, this.titleLabelX, this.titleLabelY + 60, 4210752, false);

    guiGraphics.blit(TEXTURES, 8, 19, 176, 0, 52, 228);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    this.renderTooltip(guiGraphics, mouseX, mouseY);
    if (mouseX > this.leftPos + 6 &&
        mouseX < this.leftPos + 61 &&
        mouseY > this.topPos + 18 &&
        mouseY < this.topPos + 63) {
      var tooltip = new ArrayList<Component>();
      tooltip.add(blockentity.clientFluidStack.isEmpty()
          ? TANK_EMPTY
          : blockentity.clientFluidStack.getDisplayName());
      var amount = FORMAT.format(blockentity.clientFluidStack.getAmount());
      var capacity = FORMAT.format(blockentity.totalCapacity);
      tooltip.add(Component.translatable(Translations.LIQUID_AMOUNT, amount, capacity));
      float percentage = 0;
      if (blockentity.totalCapacity > 0) {
        percentage = blockentity.clientFluidStack.getAmount() * 100.0F / blockentity.totalCapacity;
      }

      var textColor = ChatFormatting.GREEN;
      if (percentage >= 60 && percentage < 90) {
        textColor = ChatFormatting.YELLOW;
      } else if (percentage >= 90) {
        textColor = ChatFormatting.RED;
      }
      tooltip.add(Component
          .translatable(Translations.LIQUID_PERCENTAGE, String.format("%.2f", percentage))
          .withStyle(textColor));
      guiGraphics.renderTooltip(font, Lists.transform(tooltip, Component::getVisualOrderText),
          mouseX, mouseY);
    }
  }
}
