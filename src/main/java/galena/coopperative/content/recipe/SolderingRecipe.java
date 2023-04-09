package galena.coopperative.content.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import galena.coopperative.Coopperative;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SolderingRecipe implements Recipe<Container> {

    static int MAX_INGREDIENTS = 4;

    private final ResourceLocation id;
    final String group;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;

    public SolderingRecipe(ResourceLocation id, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.group = group;
        this.ingredients = ingredients;
        this.result = result;
    }


    @Override
    public boolean matches(Container container, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(Container container) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<SolderingRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation(Coopperative.MOD_ID, "soldering");

        @Override
        public SolderingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            String group = GsonHelper.getAsString(jsonObject, "group", "");
            NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));

            if (ingredients.isEmpty())
                throw new JsonParseException("No ingredients for soldering recipe");
            if (ingredients.size() > SolderingRecipe.MAX_INGREDIENTS)
                throw new JsonParseException("Too many ingredients for soldering recipe. The maximum is " + SolderingRecipe.MAX_INGREDIENTS);

            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"), true, true);
            return new SolderingRecipe(id, group, result, ingredients);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        }

        @Nullable
        @Override
        public SolderingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            int size = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);

            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            ItemStack result = buffer.readItem();
            return new SolderingRecipe(id, group, result, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SolderingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients)
                ingredient.toNetwork(buffer);

            buffer.writeItem(recipe.result);
        }
    }

}