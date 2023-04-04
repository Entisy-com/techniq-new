package de.neariyeveryone.techniq.data;

import de.neariyeveryone.techniq.block.Blocks;
import de.neariyeveryone.techniq.item.Items;
//import de.neariyeveryone.techniq.recipe.CompressorRecipe;
import de.neariyeveryone.techniq.recipe.MetalPressRecipe;
import de.neariyeveryone.techniq.recipe.TestBlockRecipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public RecipeGenerator(DataGenerator output) {
        super(output);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer) {
        new MetalPressRecipe.Builder(net.minecraft.world.item.Items.IRON_INGOT, 1, 1, has(Items.ROD_PRESS.get()),
                MetalPressRecipe.MetalPressRecipeType.ROD, Items.IRON_ROD.get(), 2, 2000, 80, finishedRecipeConsumer);

        new MetalPressRecipe.Builder(net.minecraft.world.item.Items.GOLD_INGOT, 1, 1, has(Items.ROD_PRESS.get()),
                MetalPressRecipe.MetalPressRecipeType.ROD, Items.GOLD_ROD.get(), 2, 2000, 80, finishedRecipeConsumer);

        new MetalPressRecipe.Builder(net.minecraft.world.item.Items.DIAMOND, 1, 1, has(Items.ROD_PRESS.get()),
                MetalPressRecipe.MetalPressRecipeType.ROD, Items.DIAMOND_ROD.get(), 2, 2000, 80, finishedRecipeConsumer);

        new MetalPressRecipe.Builder(net.minecraft.world.item.Items.IRON_INGOT, 2, 1, has(Items.PLATE_PRESS.get()),
                MetalPressRecipe.MetalPressRecipeType.PLATE, Items.IRON_PLATE.get(), 1, 2000, 80, finishedRecipeConsumer);

        new MetalPressRecipe.Builder(net.minecraft.world.item.Items.GOLD_INGOT, 2, 1, has(Items.PLATE_PRESS.get()),
                MetalPressRecipe.MetalPressRecipeType.PLATE, Items.GOLD_PLATE.get(), 1, 2000, 80, finishedRecipeConsumer);

        new MetalPressRecipe.Builder(net.minecraft.world.item.Items.DIAMOND, 2, 1, has(Items.PLATE_PRESS.get()),
                MetalPressRecipe.MetalPressRecipeType.PLATE, Items.DIAMOND_PLATE.get(), 1, 2000, 80, finishedRecipeConsumer);

//        new CompressorRecipe.Builder(net.minecraft.world.item.Items.COAL, net.minecraft.world.item.Items.STONE, 8, 1, has(net.minecraft.world.item.Items.STONE),
//                net.minecraft.world.item.Items.COAL_ORE, 1, 2000, 120, finishedRecipeConsumer);
//
//        new CompressorRecipe.Builder(net.minecraft.world.item.Items.COPPER_INGOT, net.minecraft.world.item.Items.STONE, 8, 1, has(net.minecraft.world.item.Items.STONE),
//                net.minecraft.world.item.Items.COPPER_ORE, 1, 2000, 120, finishedRecipeConsumer);
//
//        new CompressorRecipe.Builder(net.minecraft.world.item.Items.DIAMOND, net.minecraft.world.item.Items.STONE, 8, 1, has(net.minecraft.world.item.Items.STONE),
//                net.minecraft.world.item.Items.DIAMOND_ORE, 1, 2000, 120, finishedRecipeConsumer);
//
//        new CompressorRecipe.Builder(net.minecraft.world.item.Items.EMERALD, net.minecraft.world.item.Items.STONE, 8, 1, has(net.minecraft.world.item.Items.STONE),
//                net.minecraft.world.item.Items.EMERALD_ORE, 1, 2000, 120, finishedRecipeConsumer);
//
//        new CompressorRecipe.Builder(net.minecraft.world.item.Items.GOLD_INGOT, net.minecraft.world.item.Items.STONE, 8, 1, has(net.minecraft.world.item.Items.STONE),
//                net.minecraft.world.item.Items.GOLD_ORE, 1, 2000, 120, finishedRecipeConsumer);
//
//        new CompressorRecipe.Builder(net.minecraft.world.item.Items.IRON_INGOT, net.minecraft.world.item.Items.STONE, 8, 1, has(net.minecraft.world.item.Items.STONE),
//                net.minecraft.world.item.Items.IRON_ORE, 1, 2000, 120, finishedRecipeConsumer);
//
//        new CompressorRecipe.Builder(net.minecraft.world.item.Items.LAPIS_LAZULI, net.minecraft.world.item.Items.STONE, 8, 1, has(net.minecraft.world.item.Items.STONE),
//                net.minecraft.world.item.Items.LAPIS_ORE, 1, 2000, 120, finishedRecipeConsumer);
//
//        new CompressorRecipe.Builder(net.minecraft.world.item.Items.REDSTONE, net.minecraft.world.item.Items.STONE, 8, 1, has(net.minecraft.world.item.Items.STONE),
//                net.minecraft.world.item.Items.REDSTONE_ORE, 1, 2000, 120, finishedRecipeConsumer);
//
//        new CompressorRecipe.Builder(net.minecraft.world.item.Items.QUARTZ, net.minecraft.world.item.Items.NETHERRACK, 8, 1, has(net.minecraft.world.item.Items.NETHERRACK),
//                net.minecraft.world.item.Items.NETHER_QUARTZ_ORE, 1, 2000, 120, finishedRecipeConsumer);

        new ShapedRecipeBuilder(Blocks.METAL_PRESS.get(), 1)
                .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                .define('B', net.minecraft.world.level.block.Blocks.IRON_BLOCK)
                .pattern("III").pattern("IBI").pattern("III")
                .unlockedBy("has_", has(net.minecraft.world.item.Items.IRON_BLOCK))
                .save(finishedRecipeConsumer, "metal_press_shaped");

//        new ShapedRecipeBuilder(Blocks.COMPRESSOR.get(), 1)
//                .define('B', net.minecraft.world.level.block.Blocks.IRON_BLOCK)
//                .define('R', Items.IRON_ROD.get())
//                .define('P', Items.IRON_PLATE.get())
//                .pattern("RPR").pattern("PBP").pattern("RPR")
//                .unlockedBy("has_", has(Items.IRON_PLATE.get()))
//                .save(finishedRecipeConsumer, "compressor_shaped");

        new ShapelessRecipeBuilder(Items.ROD_PRESS.get(), 1)
                .unlockedBy("has_", has(net.minecraft.world.item.Items.SHEARS))
                .requires(net.minecraft.world.item.Items.SHEARS)
                .requires(net.minecraft.world.item.Items.IRON_INGOT)
                .requires(net.minecraft.world.item.Items.STICK)
                .save(finishedRecipeConsumer, "rod_press_shapeless");

        new ShapelessRecipeBuilder(Items.PLATE_PRESS.get(), 1)
                .unlockedBy("has_", has(net.minecraft.world.item.Items.SHEARS))
                .requires(net.minecraft.world.item.Items.SHEARS)
                .requires(net.minecraft.world.item.Items.IRON_INGOT)
                .requires(net.minecraft.world.item.Items.PAPER)
                .save(finishedRecipeConsumer, "plate_press_shapeless");
    }
}
