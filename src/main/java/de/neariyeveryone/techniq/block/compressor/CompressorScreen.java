//package de.neariyeveryone.techniq.block.compressor;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.neariyeveryone.techniq.TechniqConstants;
//import de.neariyeveryone.techniq.screen.EnergyInfoArea;
//import de.neariyeveryone.utilities.NMouseUtils;
//import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Inventory;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Optional;
//
//public class CompressorScreen extends AbstractContainerScreen<CompressorMenu> {
//    private static final ResourceLocation TEXTURE = new ResourceLocation(TechniqConstants.MOD_ID,"textures/gui/metal_press.png");
//    private EnergyInfoArea energyInfoArea;
//
//    public CompressorScreen(CompressorMenu menu, Inventory inv, Component component) {
//        super(menu,inv,component);
//    }
//
//    @Override
//    protected void init() {
//        super.init();
//        assignEnergyInfoArea();
//    }
//
//    private void assignEnergyInfoArea() {
//        var x = (width - imageWidth) /2;
//        var y = (height - imageHeight) /2;
//        energyInfoArea = new EnergyInfoArea(x + 156, y + 13, menu.blockEntity.getStorage(), 8, 64);
//    }
//
//    @Override
//    protected void renderLabels(@NotNull PoseStack stack, int mouseX, int mouseY) {
//        var x = (width - imageWidth) / 2;
//        var y = (height - imageHeight) / 2;
//
//        renderEnergyAreaTooltips(stack, mouseX, mouseY, x, y);
//    }
//
//    private void renderEnergyAreaTooltips(PoseStack stack, int mouseX, int mouseY, int x, int y) {
//        if (isMouseAboveArea(mouseX, mouseY, x, y, 156, 13, 8, 64)) {
//            renderTooltip(stack, energyInfoArea.getTooltips(), Optional.empty(), mouseX - x, mouseY - y);
//        }
//    }
//
//
//    private boolean isMouseAboveArea(int mouseX, int mouseY, int x, int y, int posX, int posY, int width, int height) {
//        return NMouseUtils.isMouseOver(mouseX, mouseY, x + posX, y + posY, width, height);
//    }
//
//    @Override
//    protected void renderBg(@NotNull PoseStack stack, float partialTicks, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1, 1, 1, 1);
//        RenderSystem.setShaderTexture(0, TEXTURE);
//        var x = (width - imageWidth) / 2;
//        var y = (height - imageHeight) / 2;
//
//        blit(stack, x, y, 0, 0, imageWidth, imageHeight);
//        renderProgressArrow(stack, x, y);
//
//        energyInfoArea.draw(stack);
//    }
//
//
//    private void renderProgressArrow(PoseStack stack, int x, int y) {
//        if (!menu.isCrafting()) return;
//        blit(stack, x + 67, y + 33, 176, 0, menu.getScaledProgress(), 27);
//    }
//
//    @Override
//    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
//        renderBackground(stack);
//        super.render(stack, mouseX, mouseY, partialTicks);
//        renderTooltip(stack, mouseX, mouseY);
//    }
//}
