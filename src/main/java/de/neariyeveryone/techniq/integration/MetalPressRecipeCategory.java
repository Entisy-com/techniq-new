package de.neariyeveryone.techniq.integration;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.techniq.recipe.MetalPressRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MetalPressRecipeCategory implements IRecipeCategory<MetalPressRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(TechniqConstants.MOD_ID, "metal_press");
    public static final ResourceLocation TEXTURE = new ResourceLocation(TechniqConstants.MOD_ID, "textures/gui/metal_press.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final int offsetX = 4;
    private final int offsetY = 5;

    public MetalPressRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, offsetX, offsetY, 168, 78);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.METAL_PRESS.get()));
    }

    @Override
    public @NotNull RecipeType<MetalPressRecipe> getRecipeType() {
        return JEITechniqPlugin.METAL_PRESS_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Metal Press");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MetalPressRecipe recipe, @NotNull IFocusGroup focuses) {
        var i1 = recipe.getIngredients().get(0).getItems()[0];
        var i2 = recipe.getIngredients().get(1).getItems()[0];
        var o = recipe.getResultItem();
        i1.setCount(recipe.getInput1Count());
        i2.setCount(recipe.getInput2Count());
        o.setCount(recipe.getOutputCount());
        builder.addSlot(RecipeIngredientRole.INPUT, 61 - offsetX, 16 - offsetY).addItemStack(i1);
        builder.addSlot(RecipeIngredientRole.INPUT, 61 - offsetX, 52 - offsetY).addItemStack(i2);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 97 - offsetX, 35 - offsetY).addItemStack(o);
    }
}
