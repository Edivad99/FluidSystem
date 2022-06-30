package edivad.fluidsystem.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import edivad.edivadlib.tools.utils.FluidUtils;
import edivad.fluidsystem.Main;
import edivad.fluidsystem.blockentity.tank.ControllerTankBlockEntity;
import edivad.fluidsystem.container.ContainerTankBlockController;
import edivad.fluidsystem.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ScreenModularTank extends AbstractContainerScreen<ContainerTankBlockController> {

    private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID, "textures/gui/controller_tank_block.png");
    private final ControllerTankBlockEntity blockentity;

    private static final MutableComponent TANK_EMPTY = Component.translatable(Translations.TANK_EMPTY);

    public ScreenModularTank(ContainerTankBlockController screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 187;
        this.blockentity = screenContainer.blockentity;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURES);
        blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        //Render fluid
        FluidStack fluid = blockentity.clientFluidStack;
        int index = FluidUtils.getFluidScaled(43, fluid, blockentity.totalCapacity);
        TextureAtlasSprite fluidTexture = FluidUtils.getFluidTexture(fluid);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        FluidUtils.color(FluidUtils.getLiquidColorWithBiome(fluid, blockentity));
        blit(poseStack, this.leftPos + 8, this.topPos + 19 + index, 176, 52, 43 - index, fluidTexture);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);
        this.font.draw(poseStack, Component.translatable(Translations.TANKS_BLOCK).append(String.valueOf(blockentity.tanksBlock)), this.titleLabelX, this.titleLabelY + 60, 4210752);
        //Render indicator
        RenderSystem.setShaderTexture(0, TEXTURES);
        blit(poseStack, 8, 19, 176, 0, 52, 228);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        if(mouseX > this.leftPos + 6 && mouseX < this.leftPos + 61 && mouseY > this.topPos + 18 && mouseY < this.topPos + 63) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(blockentity.clientFluidStack.isEmpty() ? TANK_EMPTY : blockentity.clientFluidStack.getDisplayName());
            DecimalFormat f = new DecimalFormat("#,##0");
            tooltip.add(Component.translatable(Translations.LIQUID_AMOUNT, f.format(blockentity.clientFluidStack.getAmount()), f.format(blockentity.totalCapacity)));
            float percentage = 0;
            if(blockentity.totalCapacity > 0)
                percentage = blockentity.clientFluidStack.getAmount() * 100.0F / blockentity.totalCapacity;

            ChatFormatting textColor = percentage < 60 ? ChatFormatting.GREEN : percentage < 90 ? ChatFormatting.YELLOW : ChatFormatting.RED;
            tooltip.add(Component.translatable(Translations.LIQUID_PERCENTAGE, String.format("%.2f", percentage)).append("%").withStyle(textColor));
            this.renderTooltip(poseStack, Lists.transform(tooltip, Component::getVisualOrderText), mouseX, mouseY);
        }
    }
}
