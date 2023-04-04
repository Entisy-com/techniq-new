package de.neariyeveryone.techniq.recipe;

import de.neariyeveryone.techniq.TechniqConstants;
//import de.neariyeveryone.techniq.recipe.CompressorRecipe;
import de.neariyeveryone.techniq.recipe.MetalPressRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, TechniqConstants.MOD_ID);

    public static final RegistryObject<RecipeType<MetalPressRecipe>> METAL_PRESS_RECIPE_TYPE = registerType(TechniqConstants.METAL_PRESS);
//    public static final RegistryObject<RecipeType<CompressorRecipe>> COMPRESSOR_RECIPE_TYPE = registerType(TechniqConstants.COMPRESSOR);

    private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerType(ResourceLocation name) {
        return RECIPE_TYPES.register(name.getPath(), () -> RecipeType.simple(name));
    }
}
