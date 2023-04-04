package de.neariyeveryone.techniq.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.techniq.item.Items;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class MetalPressRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> ingredients;
    private final int requiredEnergy;
    private final int processTime;
    private final int input1Count;
    private final int input2Count;
    private final int outputCount;

    public MetalPressRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> ingredients,
                            int requiredEnergy, int processTime, int input1Count, int input2Count, int outputCount) {
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.requiredEnergy = requiredEnergy;
        this.processTime = processTime;
        this.input1Count = input1Count;
        this.input2Count = input2Count;
        this.outputCount = outputCount;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, @NotNull Level level) {
        return ingredients.get(0).test(container.getItem(0)) && ingredients.get(1).test(container.getItem(1)) &&
                container.getItem(0).getCount() >= getInput1Count() && container.getItem(1).getCount() >= getInput2Count();
    }

    public int getRequiredEnergy() {
        return requiredEnergy;
    }

    public int getProcessTime() {
        return processTime;
    }

    public int getInput1Count() {
        return input1Count;
    }

    public int getInput2Count() {
        return input2Count;
    }

    public int getOutputCount() {
        return outputCount;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer container) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 8;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Recipes.METAL_PRESS_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypes.METAL_PRESS_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<MetalPressRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(TechniqConstants.MOD_ID, "metal_press_recipe");

        @Override
        public @NotNull MetalPressRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            var output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "output"));

            var ingredients = GsonHelper.getAsJsonArray(serializedRecipe, "ingredients");
            var inputs = NonNullList.withSize(2, Ingredient.EMPTY);
            var requiredEnergy = serializedRecipe.get("energy").getAsInt();
            var processTime = serializedRecipe.get("processTime").getAsInt();
            var input1Count = ingredients.get(0).getAsJsonObject().get("count") != null ?
                    ingredients.get(0).getAsJsonObject().get("count").getAsInt() : 1;
            var input2Count = ingredients.get(1).getAsJsonObject().get("count") != null ?
                    ingredients.get(1).getAsJsonObject().get("count").getAsInt() : 1;
            var outputCount = output.getCount();

            for (var i = 0; i < inputs.size(); i++)
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));

            return new MetalPressRecipe(recipeId, output, inputs, requiredEnergy, processTime, input1Count, input2Count, outputCount);
        }

        @Override
        public @Nullable MetalPressRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            var inputs = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);
            var requiredEnergy = buffer.readInt();
            var processTime = buffer.readInt();

            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            var input1Count = inputs.get(0).getItems()[0].getCount();
            var input2Count = inputs.get(1).getItems()[0].getCount();

            var output = buffer.readItem();
            var outputCount = output.getCount();

            return new MetalPressRecipe(recipeId, output, inputs, requiredEnergy, processTime, input1Count, input2Count, outputCount);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull MetalPressRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            buffer.writeInt(recipe.requiredEnergy);
            buffer.writeInt(recipe.getProcessTime());

            buffer.writeInt(recipe.getInput1Count());
            buffer.writeInt(recipe.getInput2Count());

            for (var ingredient : recipe.getIngredients())
                ingredient.toNetwork(buffer);

            buffer.writeItemStack(recipe.getResultItem(), false);
            buffer.writeInt(recipe.getOutputCount());
        }
    }

    public static class Builder implements RecipeBuilder {
        private final MetalPressRecipeType type;
        private final Item result;
        private final List<Ingredient> ingredients = new ArrayList<>();
        private final int requiredEnergy;
        private final int processTime;
        private final Advancement.Builder advancement = Advancement.Builder.advancement();
        private final int input1Count;
        private final int input2Count;
        private final int outputCount;

        @Nullable
        private String group;

        public Builder(Item input, int input1Count, int input2Count, CriterionTriggerInstance trigger, MetalPressRecipeType type, Item result,
                       int outputCount, int requiredEnergy, int processTime, Consumer<FinishedRecipe> consumer) {
            this.ingredients.add(Ingredient.of(input));
            this.ingredients.add(Ingredient.of(type.getItem()));
            this.advancement.addCriterion("has_", trigger);
            this.type = type;
            this.result = result;
            this.requiredEnergy = requiredEnergy;
            this.processTime = processTime;
            this.input1Count = input1Count;
            this.input2Count = input2Count;
            this.outputCount = outputCount;
            this.save(consumer, new ResourceLocation(TechniqConstants.MOD_ID, String.format("%s_from_%s_%s",
                    Objects.requireNonNull(Registry.ITEM.getKey(result)).getPath(), Objects.requireNonNull(
                            Registry.ITEM.getKey(ingredients.get(0).getItems()[0].getItem())).getPath(), RecipeTypes.METAL_PRESS_RECIPE_TYPE)));
        }

        @Override
        public @NotNull Builder unlockedBy(@NotNull String criterion, @NotNull CriterionTriggerInstance trigger) {
            return this;
        }

        @Override
        public @NotNull Builder group(@Nullable String tag) {
            this.group = tag;
            return this;
        }

        public @NotNull Item getResult() {
            return this.result;
        }

        public void save(@NotNull Consumer<FinishedRecipe> consumer) {
            this.save(consumer, new ResourceLocation(TechniqConstants.MOD_ID, String.format("%s_from_%s_%s",
                    Objects.requireNonNull(Registry.ITEM.getKey(result)).getPath(), Objects.requireNonNull(
                            Registry.ITEM.getKey(ingredients.get(0).getItems()[0].getItem())).getPath(), RecipeTypes.METAL_PRESS_RECIPE_TYPE)));
        }

        public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation id) {
            this.ensureValid(id);
            this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
            consumer.accept(new Result(id, this.group == null ? "" : this.group, this.ingredients, this.result,
                    this.processTime, this.advancement, new ResourceLocation(id.getNamespace(),
                    String.format("recipes/%s/%s", this.result.getItemCategory().getRecipeFolderName(), id.getPath())),
                    MetalPressRecipe.Serializer.INSTANCE, this.requiredEnergy, this.input1Count, this.input2Count, this.outputCount));
        }

        private void ensureValid(ResourceLocation res) {
            if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + res);
            }
        }

        public record Result(ResourceLocation id, String group, List<Ingredient> ingredients, Item result,
                             int processTime, Advancement.Builder advancement,
                             ResourceLocation advancementId, Serializer serializer,
                             int requiredEnergy, int input1Count, int input2Count,
                             int outputCount) implements FinishedRecipe {

            @Deprecated
            @Override
            public void serializeRecipeData(JsonObject json) {
                if (!this.group.isEmpty()) {
                    json.addProperty("group", this.group);
                }
                var JSON_ingredients = new JsonArray();
                var JSON_ingredient = new JsonObject();
                JSON_ingredient.addProperty("item", Registry.ITEM.getKey(ingredients.get(0).getItems()[0].getItem()).toString());
                if (input1Count > 1)
                    JSON_ingredient.addProperty("count", input1Count);
                JSON_ingredients.add(JSON_ingredient);
                JSON_ingredient = new JsonObject();
                JSON_ingredient.addProperty("item", Registry.ITEM.getKey(ingredients.get(1).getItems()[0].getItem()).toString());
                if (input2Count > 1)
                    JSON_ingredient.addProperty("count", input2Count);
                JSON_ingredients.add(JSON_ingredient);
                json.add("ingredients", JSON_ingredients);
                json.addProperty("energy", this.requiredEnergy);
                json.addProperty("processTime", this.processTime);
                var JSON_item = new JsonObject();
                JSON_item.addProperty("item", Registry.ITEM.getKey(this.result).toString());
                if (outputCount > 1)
                    JSON_item.addProperty("count", outputCount);
                json.add("output", JSON_item);
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return this.id;
            }

            @Override
            public @NotNull Serializer getType() {
                return this.serializer;
            }

            @Override
            public JsonObject serializeAdvancement() {
                return this.advancement.serializeToJson();
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return this.advancementId;
            }
        }
    }

    public static enum MetalPressRecipeType {
        ROD(Items.ROD_PRESS.get()),
        PLATE(Items.PLATE_PRESS.get());

        final Item item;

        MetalPressRecipeType(Item item) {
            this.item = item;
        }

        public Item getItem() {
            return item;
        }
    }
}
