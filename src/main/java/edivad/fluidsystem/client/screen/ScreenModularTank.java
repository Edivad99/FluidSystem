package edivad.fluidsystem.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import edivad.fluidsystem.Main;
import edivad.fluidsystem.container.ContainerTankBlockController;
import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import edivad.fluidsystem.tools.Translations;
import edivad.fluidsystem.tools.utils.FluidUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ScreenModularTank extends AbstractContainerScreen<ContainerTankBlockController>
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID, "textures/gui/controller_tank_block.png");
    private final TileEntityControllerTankBlock tile;

    public ScreenModularTank(ContainerTankBlockController screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 187;
        this.tile = screenContainer.tile;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        RenderSystem.setShaderTexture(0, TEXTURES);
        blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        //Render fluid
        FluidStack fluid = tile.clientFluidStack;
        int index = FluidUtils.getFluidScaled(43, fluid, tile.totalCapacity);
        TextureAtlasSprite fluidTexture = FluidUtils.getFluidTexture(fluid);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        FluidUtils.color(FluidUtils.getLiquidColorWithBiome(fluid, tile));
        blit(poseStack, this.leftPos + 8, this.topPos + 19 + index, 176, 52, 43 - index, fluidTexture);
    }

    @Override
    protected void renderLabels(PoseStack mStack, int mouseX, int mouseY)
    {
        this.font.draw(mStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);
        this.font.draw(mStack, new TranslatableComponent(Translations.TANKS_BLOCK).append(String.valueOf(tile.tanksBlock)), this.titleLabelX, this.titleLabelY + 60, 4210752);
        //Render indicator
        RenderSystem.setShaderTexture(0, TEXTURES);
        blit(mStack, 8, 19, 176, 0, 52, 228);
    }

    @Override
    public void render(PoseStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(mStack);
        super.render(mStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(mStack, mouseX, mouseY);
        if(mouseX > this.leftPos + 6 && mouseX < this.leftPos + 61 && mouseY > this.topPos + 18 && mouseY < this.topPos + 63)
        {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(tile.clientFluidStack.isEmpty() ? new TranslatableComponent(Translations.TANK_EMPTY) : tile.clientFluidStack.getDisplayName());
            DecimalFormat f = new DecimalFormat("#,##0");
            tooltip.add(new TranslatableComponent(Translations.LIQUID_AMOUNT, f.format(tile.clientFluidStack.getAmount()), f.format(tile.totalCapacity)));
            float percentage;
            if(tile.totalCapacity == 0)
                percentage = 0;
            else
                percentage = tile.clientFluidStack.getAmount() * 100.0F / tile.totalCapacity;
            tooltip.add(new TranslatableComponent(Translations.LIQUID_PERCENTAGE, String.format("%.2f", percentage)).append("%").withStyle(percentage < 60 ? ChatFormatting.GREEN : percentage < 90 ? ChatFormatting.YELLOW : ChatFormatting.RED));
            this.renderTooltip(mStack, Lists.transform(tooltip, Component::getVisualOrderText), mouseX, mouseY);
        }
    }
}
