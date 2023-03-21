package de.neariyeveryone.techniq.recipe;

import de.neariyeveryone.techniq.TechniqConstants;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Recipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, TechniqConstants.MOD_ID);

    public static final RegistryObject<RecipeSerializer<TestBlockRecipe>> TEST_BLOCK_SERIALIZER = RECIPE_SERIALIZERS.register(
            "test", () -> TestBlockRecipe.Serializer.INSTANCE);
}
