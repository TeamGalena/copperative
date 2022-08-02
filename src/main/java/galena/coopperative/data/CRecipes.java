package galena.coopperative.data;

import galena.coopperative.content.index.CBlocks;
import galena.coopperative.data.provider.CRecipeProvider;
import galena.coopperative.content.index.CItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class CRecipes extends CRecipeProvider {

    protected static final TagKey<Item> COPPER_INGOT = Tags.Items.INGOTS_COPPER;
    protected static final TagKey<Item> REDSTONE_DUST = Tags.Items.DUSTS_REDSTONE;
    protected static final Item REDSTONE_TORCH = Items.REDSTONE_TORCH;

    public CRecipes(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        compact(Items.COPPER_INGOT, CItems.COPPER_NUGGET.get()).save(consumer, "copper_ingot_from_nuggets");
        unCompact(CItems.COPPER_NUGGET.get(), Items.COPPER_INGOT).save(consumer);

        compact(CBlocks.PATINA_BLOCK.get(), CItems.PATINA.get()).save(consumer);
        unCompact(CItems.PATINA.get(), CBlocks.PATINA_BLOCK.get()).save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.REPEATER)
                .pattern("ABA")
                .pattern("CCC")
                .define('A', REDSTONE_TORCH)
                .define('B', REDSTONE_DUST)
                .define('C', COPPER_INGOT)
                .unlockedBy("has_redstone_dust", has(REDSTONE_DUST))
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.COMPARATOR)
                .pattern(" A ")
                .pattern("ABA")
                .pattern("CCC")
                .define('A', REDSTONE_TORCH)
                .define('B', Items.QUARTZ)
                .define('C', COPPER_INGOT)
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.PISTON)
                .pattern("AAA")
                .pattern("BCB")
                .pattern("BDB")
                .define('A', ItemTags.PLANKS)
                .define('B', COPPER_INGOT)
                .define('C', Tags.Items.INGOTS_IRON)
                .define('D', REDSTONE_DUST)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.OBSERVER)
                .pattern("AAA")
                .pattern("BBC")
                .pattern("AAA")
                .define('A', COPPER_INGOT)
                .define('B', REDSTONE_DUST)
                .define('C', Items.QUARTZ)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.DISPENSER)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("ACA")
                .define('A', COPPER_INGOT)
                .define('B', Items.BOW)
                .define('C', REDSTONE_DUST)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.DROPPER)
                .pattern("AAA")
                .pattern("A A")
                .pattern("ABA")
                .define('A', COPPER_INGOT)
                .define('B', REDSTONE_DUST)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.LEVER)
                .pattern("A")
                .pattern("B")
                .define('A', Tags.Items.RODS_WOODEN)
                .define('B', COPPER_INGOT)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.POWERED_RAIL, 6)
                .pattern("A A")
                .pattern("ABA")
                .pattern("ACA")
                .define('A', COPPER_INGOT)
                .define('B', Tags.Items.RODS_WOODEN)
                .define('C', REDSTONE_DUST)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(consumer);
    }
}
