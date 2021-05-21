package edivad.fluidsystem.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import edivad.fluidsystem.Main;
import edivad.fluidsystem.container.ContainerTankBlockController;
import edivad.fluidsystem.tile.tank.TileEntityControllerTankBlock;
import edivad.fluidsystem.tools.Translations;
import edivad.fluidsystem.tools.utils.FluidUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ScreenModularTank extends ContainerScreen<ContainerTankBlockController>
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(Main.MODID, "textures/gui/controller_tank_block.png");
    private final TileEntityControllerTankBlock tile;

    public ScreenModularTank(ContainerTankBlockController screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.xSize = 176;
        this.ySize = 187;
        this.tile = screenContainer.tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack mStack, float partialTicks, int mouseX, int mouseY)
    {
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        blit(mStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        //Render fluid
        FluidStack fluid = tile.clientFluidStack;
        int index = FluidUtils.getFluidScaled(43, fluid, tile.totalCapacity);
        TextureAtlasSprite fluidTexture = FluidUtils.getFluidTexture(fluid);
        this.minecraft.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        FluidUtils.color(FluidUtils.getLiquidColorWithBiome(fluid, tile));
        blit(mStack, this.guiLeft + 8, this.guiTop + 19 + index, 176, 52, 43 - index, fluidTexture);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack mStack, int mouseX, int mouseY)
    {
        this.font.drawText(mStack, this.title, this.titleX, this.titleY, 4210752);
        this.font.drawText(mStack, new TranslationTextComponent(Translations.TANKS_BLOCK).appendString(String.valueOf(tile.tanksBlock)), this.titleX, this.titleY + 60, 4210752);
        //Render indicator
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        blit(mStack, 8, 19, 176, 0, 52, 228);
    }

    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(mStack);
        super.render(mStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(mStack, mouseX, mouseY);
        if(mouseX > this.guiLeft + 6 && mouseX < this.guiLeft + 61 && mouseY > this.guiTop + 18 && mouseY < this.guiTop + 63)
        {
            List<ITextComponent> tooltip = new ArrayList<>();
            tooltip.add(tile.clientFluidStack.isEmpty() ? new TranslationTextComponent(Translations.TANK_EMPTY) : tile.clientFluidStack.getDisplayName());
            DecimalFormat f = new DecimalFormat("#,##0");
            tooltip.add(new TranslationTextComponent(Translations.LIQUID_AMOUNT, f.format(tile.clientFluidStack.getAmount()), f.format(tile.totalCapacity)));
            float percentage;
            if(tile.totalCapacity == 0)
                percentage = 0;
            else
                percentage = tile.clientFluidStack.getAmount() * 100.0F / tile.totalCapacity;
            tooltip.add(new TranslationTextComponent(Translations.LIQUID_PERCENTAGE, String.format("%.2f", percentage)).appendString("%").mergeStyle(percentage < 60 ? TextFormatting.GREEN : percentage < 90 ? TextFormatting.YELLOW : TextFormatting.RED));
            this.renderTooltip(mStack, Lists.transform(tooltip, ITextComponent::func_241878_f), mouseX, mouseY);
        }
    }
}
