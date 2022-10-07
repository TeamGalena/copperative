package galena.coopperative.data.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.function.Supplier;

public class CRecipeProvider extends RecipeProvider {

    public CRecipeProvider(DataGenerator gen) {
        super(gen);
    }

    public ShapedRecipeBuilder compact(ItemLike itemOut, ItemLike itemIn) {
        return ShapedRecipeBuilder.shaped(itemOut)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', itemIn)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(itemIn.asItem()).getPath(), has(itemIn));
    }

    public ShapelessRecipeBuilder unCompact(ItemLike itemOut, ItemLike itemIn) {
        return ShapelessRecipeBuilder.shapeless(itemOut, 9)
                .requires(itemIn)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(itemIn.asItem()).getPath(), has(itemIn));
    }

    public ShapedRecipeBuilder quadTransform(ItemLike itemIn, ItemLike itemOut) {
        return ShapedRecipeBuilder.shaped(itemOut, 4)
                .pattern("AA")
                .pattern("AA")
                .define('A', itemIn)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(itemIn.asItem()).getPath(), has(itemIn));
    }

    public ShapedRecipeBuilder quadTransform(Supplier<? extends Block> blockIn, Supplier<? extends Block> blockOut) {
        return quadTransform(blockIn.get(), blockOut.get());
    }

    public ShapedRecipeBuilder quadTransform(ItemLike itemIn, Supplier<? extends Block> blockOut) {
        return quadTransform(itemIn, blockOut.get());
    }

    public ShapedRecipeBuilder door(ItemLike itemIn, Supplier<? extends DoorBlock> door) {
        return ShapedRecipeBuilder.shaped(door.get())
                .pattern("AA")
                .pattern("AA")
                .pattern("AA")
                .define('A', itemIn)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(itemIn.asItem()).getPath(), has(itemIn));
    }

    public ShapedRecipeBuilder trapdoor(ItemLike itemIn, Supplier<? extends TrapDoorBlock> trapdoor) {
        return ShapedRecipeBuilder.shaped(trapdoor.get())
                .pattern("AA")
                .pattern("AA")
                .define('A', itemIn)
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(itemIn.asItem()).getPath(), has(itemIn));
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
