package de.neariyeveryone.techniq.integration;

import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.techniq.recipe.RecipeTypes;
//import de.neariyeveryone.techniq.recipe.CompressorRecipe;
import de.neariyeveryone.techniq.recipe.MetalPressRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEITechniqPlugin implements IModPlugin {
//    public static RecipeType<CompressorRecipe> COMPRESSOR_TYPE = new RecipeType<>(CompressorRecipeCategory.UID, CompressorRecipe.class);
    public static RecipeType<MetalPressRecipe> METAL_PRESS_TYPE = new RecipeType<>(MetalPressRecipeCategory.UID, MetalPressRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(TechniqConstants.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
//        registration.addRecipeCategories(new CompressorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MetalPressRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
//        registration.addRecipeCatalyst(new ItemStack(Blocks.COMPRESSOR.get()), COMPRESSOR_TYPE);
        registration.addRecipeCatalyst(new ItemStack(Blocks.METAL_PRESS.get()), METAL_PRESS_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();

//        var compressorRecipes = recipeManager.getAllRecipesFor(RecipeTypes.COMPRESSOR_RECIPE_TYPE.get());
        var metalPressRecipes = recipeManager.getAllRecipesFor(RecipeTypes.METAL_PRESS_RECIPE_TYPE.get());

//        registration.addRecipes(COMPRESSOR_TYPE, compressorRecipes);
        registration.addRecipes(METAL_PRESS_TYPE, metalPressRecipes);
    }
}
