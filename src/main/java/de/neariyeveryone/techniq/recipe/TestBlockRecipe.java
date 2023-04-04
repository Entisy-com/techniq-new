package de.neariyeveryone.techniq.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.neariyeveryone.techniq.TechniqConstants;
import de.neariyeveryone.utilities.FluidJSONUtil;
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
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestBlockRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final List<Ingredient> ingredients;
    private final FluidStack fluid;
    private final int requiredEnergy;
    private final float experience;
    private final int processTime;

    public TestBlockRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> ingredients,
                           FluidStack fluid, int requiredEnergy, float experience, int processTime) {
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.fluid = fluid;
        this.requiredEnergy = requiredEnergy;
        this.experience = experience;
        this.processTime = processTime;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, @NotNull Level level) {
        if (level.isClientSide()) return false;

        return ingredients.get(0).test(container.getItem(1));
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public int getRequiredEnergy() {
        return requiredEnergy;
    }

    public float getExperience() {
        return experience;
    }

    public int getProcessTime() {
        return processTime;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer container) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
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
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<TestBlockRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "test";
    }

    public static class Serializer implements RecipeSerializer<TestBlockRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(TechniqConstants.MOD_ID, "test");

        @Override
        public @NotNull TestBlockRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(serializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            FluidStack fluid = FluidJSONUtil.readFluid(serializedRecipe.get("fluid").getAsJsonObject());
            int requiredEnergy = serializedRecipe.get("energy").getAsInt();
            float experience = serializedRecipe.get("experience").getAsFloat();
            int processTime = serializedRecipe.get("processTime").getAsInt();

            for (int i = 0; i < inputs.size(); i++)
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));

            return new TestBlockRecipe(recipeId, output, inputs, fluid, requiredEnergy, experience, processTime);
        }

        @Override
        public @Nullable TestBlockRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);
            FluidStack fluid = buffer.readFluidStack();
            int requiredEnergy = buffer.readInt();
            float experience = buffer.readFloat();
            int processTime = buffer.readInt();

            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            ItemStack output = buffer.readItem();
            return new TestBlockRecipe(recipeId, output, inputs, fluid, requiredEnergy, experience, processTime);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull TestBlockRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            buffer.writeFluidStack(recipe.fluid);
            buffer.writeInt(recipe.requiredEnergy);
            buffer.writeFloat(recipe.experience);
            buffer.writeInt(recipe.getProcessTime());

            for (Ingredient ingredient : recipe.getIngredients())
                ingredient.toNetwork(buffer);
            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Item result;
        private final List<Ingredient> ingredients = new ArrayList<>();
        private final FluidStack fluid;
        private final int requiredEnergy;
        private final float experience;
        private final int processTime;
        private final Advancement.Builder advancement = Advancement.Builder.advancement();
        private final Serializer serializer;
        @Nullable
        private String group;


        public Builder(Item result, FluidStack fluid, int requiredEnergy, float experience, int processTime,
                       Serializer serializer) {
            this.result = result;
            this.fluid = fluid;
            this.requiredEnergy = requiredEnergy;
            this.experience = experience;
            this.processTime = processTime;
            this.serializer = serializer;
        }

        @Override
        public @NotNull Builder unlockedBy(@NotNull String criterion, @NotNull CriterionTriggerInstance trigger) {
            this.advancement.addCriterion(criterion, trigger);
            return this;
        }

        public Builder requires(Ingredient i) {
            this.ingredients.add(i);
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

        @Override
        public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull String id) {
            this.save(consumer,new ResourceLocation(TechniqConstants.MOD_ID, id));
        }

        @Override
        public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation id) {
            this.ensureValid(id);
            this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
            consumer.accept(new Builder.Result(id, this.group == null ? "" : this.group, this.ingredients, this.result,
                    this.experience, this.processTime, this.advancement, new ResourceLocation(id.getNamespace(),
                    String.format("recipes/%s/%s", this.result.getItemCategory().getRecipeFolderName(), id.getPath())),
                    this.serializer, this.fluid, this.requiredEnergy));
        }

        private void ensureValid(ResourceLocation res) {
            if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + res);
            }
        }

        public static record Result(ResourceLocation id, String group, List<Ingredient> ingredients, Item result,
                                    float experience, int processTime, Advancement.Builder advancement,
                                    ResourceLocation advancementId, Serializer serializer, FluidStack fluid,
                                    int requiredEnergy) implements FinishedRecipe {

            @Deprecated
            @Override
            public void serializeRecipeData(JsonObject json) {
                if (!this.group.isEmpty()) {
                    json.addProperty("group", this.group);
                }
                JsonArray JSON_ingredients = new JsonArray();
                ingredients.forEach(ingredient -> JSON_ingredients.add(ingredient.toJson()));
                json.add("ingredients", JSON_ingredients);
                json.add("fluid", FluidJSONUtil.toJson(this.fluid));
                json.addProperty("energy", this.requiredEnergy);
                json.addProperty("experience", this.experience);
                json.addProperty("processTime", this.processTime);
                JsonObject JSON_item = new JsonObject();
                JSON_item.addProperty("item", Registry.ITEM.getKey(this.result).toString());
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
}
