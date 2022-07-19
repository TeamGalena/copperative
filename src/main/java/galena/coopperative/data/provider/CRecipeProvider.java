package galena.coopperative.data.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CRecipeProvider extends RecipeProvider {

    public CRecipeProvider(DataGenerator gen) {
        super(gen);
    }

    public ShapedRecipeBuilder compact(Item itemOut, Item itemIn) {
        return ShapedRecipeBuilder.shaped(itemOut)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', itemIn)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(itemIn).getPath(), has(itemIn));
    }

    public ShapelessRecipeBuilder unCompact(Item itemOut, Item itemIn) {
        return ShapelessRecipeBuilder.shapeless(itemOut, 9)
                .requires(itemIn)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(itemIn).getPath(), has(itemIn));
    }

    public ShapelessRecipeBuilder makeWaxed(Supplier<Block> blockOut, Block blockIn) {
        return ShapelessRecipeBuilder.shapeless(blockOut.get())
                .requires(blockIn)
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn), has(blockIn))
                .unlockedBy("has_honeycomb", has(Items.HONEYCOMB));
    }

    public ShapelessRecipeBuilder makeWaxed(Supplier<Block> blockOut, Supplier<Block> blockIn) {
        return makeWaxed(blockOut, blockIn.get());
    }
}
