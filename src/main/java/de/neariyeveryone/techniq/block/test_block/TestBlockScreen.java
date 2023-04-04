//package de.neariyeveryone.techniq.block.test_block;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.neariyeveryone.techniq.TechniqConstants;
//import de.neariyeveryone.techniq.screen.EnergyInfoArea;
//import de.neariyeveryone.techniq.screen.FluidTankRenderer;
//import de.neariyeveryone.utilities.NMouseUtils;
//import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.item.TooltipFlag;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Optional;
//
//public class TestBlockScreen extends AbstractContainerScreen<TestBlockMenu> {
//    private static final ResourceLocation TEXTURE = new ResourceLocation(TechniqConstants.MOD_ID,
//            "textures/gui/test_block_gui.png");
//    private EnergyInfoArea energyInfoArea;
//    private FluidTankRenderer fluidTankRenderer;
//
//    public TestBlockScreen(TestBlockMenu menu, Inventory inv, Component component) {
//        super(menu, inv, component);
//    }
//
//    @Override
//    protected void init() {
//        super.init();
//        assignEnergyInfoArea();
//        assignFluidTankRenderer();
//    }
//
//    private void assignFluidTankRenderer() {
//        fluidTankRenderer = new FluidTankRenderer(64_000, true, 16, 61);
//    }
//
//    private void assignEnergyInfoArea() {
//        int x = (width - imageWidth) / 2;
//        int y = (height - imageHeight) / 2;
//        energyInfoArea = new EnergyInfoArea(x + 156, y + 13, menu.blockEntity.getStorage(), 8, 64);
//    }
//
//    @Override
//    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
//        int x = (width - imageWidth) / 2;
//        int y = (height - imageHeight) / 2;
//
//        renderEnergyAreaTooltips(stack, mouseX, mouseY, x, y);
//        renderFluidAreaTooltips(stack, mouseX, mouseY, x, y);
//    }
//
//    private void renderEnergyAreaTooltips(PoseStack stack, int mouseX, int mouseY, int x, int y) {
//        if (isMouseAboveArea(mouseX, mouseY, x, y, 156, 13, 8, 64)) {
//            renderTooltip(stack, energyInfoArea.getTooltips(), Optional.empty(), mouseX - x, mouseY - y);
//        }
//    }
//
//    private void renderFluidAreaTooltips(PoseStack stack, int mouseX, int mouseY, int x, int y) {
//        if (isMouseAboveArea(mouseX, mouseY, x, y, 55, 15)) {
//            renderTooltip(stack, fluidTankRenderer.getTooltip(menu.getFluidStack(), TooltipFlag.Default.NORMAL),
//                    Optional.empty(), mouseX - x, mouseY - y);
//        }
//    }
//
//    private boolean isMouseAboveArea(int mouseX, int mouseY, int x, int y, int posX, int posY) {
//        return NMouseUtils.isMouseOver(mouseX, mouseY, x + posX, y + posY, fluidTankRenderer.getWidth(), fluidTankRenderer.getHeight());
//    }
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
//        int x = (width - imageWidth) / 2;
//        int y = (height - imageHeight) / 2;
//
//        blit(stack, x, y, 0, 0, imageWidth, imageHeight);
//        renderProgressArrow(stack, x, y);
//
//        energyInfoArea.draw(stack);
//        fluidTankRenderer.render(stack, x + 55, y + 15, menu.getFluidStack());
//    }
//
//    private void renderProgressArrow(PoseStack stack, int x, int y) {
//        if (!menu.isCrafting()) return;
//        blit(stack, x + 105, y + 33, 176, 0, 8, menu.getScaledProgress());
//    }
//
//    @Override
//    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
//        renderBackground(stack);
//        super.render(stack, mouseX, mouseY, partialTicks);
//        renderTooltip(stack, mouseX, mouseY);
//    }
//}
