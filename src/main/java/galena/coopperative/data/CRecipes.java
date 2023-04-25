package galena.coopperative.data;

import galena.coopperative.Coopperative;
import galena.coopperative.data.provider.CRecipeProvider;
import galena.coopperative.index.CBlocks;
import galena.coopperative.index.CItems;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CRecipes extends CRecipeProvider {

    protected static final TagKey<Item> COPPER_INGOT = Tags.Items.INGOTS_COPPER;
    protected static final TagKey<Item> REDSTONE_DUST = Tags.Items.DUSTS_REDSTONE;
    protected static final Item REDSTONE_TORCH = Items.REDSTONE_TORCH;

    public CRecipes(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

        compact(Items.COPPER_INGOT, CItems.COPPER_NUGGET.get()).save(consumer, "copper_ingot_from_nuggets");
        unCompact(CItems.COPPER_NUGGET.get(), Items.COPPER_INGOT).save(consumer);

        compact(CBlocks.PATINA_BLOCK.get(), CItems.PATINA.get()).save(consumer);
        unCompact(CItems.PATINA.get(), CBlocks.PATINA_BLOCK.get()).save(consumer);

        door(Items.COPPER_INGOT, CBlocks.COPPER_DOORS.get(0)).save(consumer);
        trapdoor(Items.COPPER_INGOT, CBlocks.COPPER_TRAPDOORS.get(0)).save(consumer);

        quadTransform(Items.CUT_COPPER, CBlocks.COPPER_BRICKS.get(0));

        var cutCopper = new Block[]{Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER};
        var cutWaxedCopper = new Block[]{Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER};
        var cutCopperSlabs = new Block[]{Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB};
        var cutWaxedCopperSlabs = new Block[]{Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB};

        for (int i = 0; i < CBlocks.COPPER_PILLAR.size(); i++) {
            ShapedRecipeBuilder.shaped(CBlocks.COPPER_PILLAR.get(i).get(), 2)
                    .pattern("A")
                    .pattern("A")
                    .define('A', cutCopper[i])
                    .unlockedBy("has_cut_copper", has(cutCopper[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(CBlocks.WAXED_COPPER_PILLAR.get(i).get(), 2)
                    .pattern("A")
                    .pattern("A")
                    .define('A', cutWaxedCopper[i])
                    .unlockedBy("has_cut_copper", has(cutWaxedCopper[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(CBlocks.COPPER_BRICKS.get(i).get(), 4)
                    .pattern("AA")
                    .pattern("AA")
                    .define('A', cutCopper[i])
                    .unlockedBy("has_cut_copper", has(cutCopper[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(CBlocks.WAXED_COPPER_BRICKS.get(i).get(), 4)
                    .pattern("AA")
                    .pattern("AA")
                    .define('A', cutWaxedCopper[i])
                    .unlockedBy("has_cut_copper", has(cutWaxedCopper[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(CBlocks.COPPER_TILES.get(i).get())
                    .pattern("A")
                    .pattern("A")
                    .define('A', cutCopperSlabs[i])
                    .unlockedBy("has_cut_copper", has(cutCopperSlabs[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(CBlocks.WAXED_COPPER_TILES.get(i).get())
                    .pattern("A")
                    .pattern("A")
                    .define('A', cutWaxedCopperSlabs[i])
                    .unlockedBy("has_cut_copper", has(cutWaxedCopperSlabs[i]))
                    .save(consumer);
        }

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

        ShapedRecipeBuilder.shaped(CBlocks.TOGGLER.get(0).get())
                .pattern(" A ")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', REDSTONE_DUST)
                .define('C', COPPER_INGOT)
                .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(CBlocks.HEADLIGHT.get(0).get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("ACA")
                .define('A', COPPER_INGOT)
                .define('B', Items.SPYGLASS)
                .define('C', Items.REDSTONE_LAMP)
                .unlockedBy("has_spyglass", has(Items.SPYGLASS))
                .unlockedBy("has_redstone_lamp", has(Items.REDSTONE_LAMP))
                .save(consumer);

        CBlocks.WAXED_BLOCKS.get().forEach((unwaxed, waxed) -> {
            var id = Registry.BLOCK.getKey(waxed);
            ShapelessRecipeBuilder.shapeless(waxed)
                    .requires(unwaxed)
                    .requires(Items.HONEYCOMB)
                    .unlockedBy("has_unwaxed", has(unwaxed))
                    .save(consumer, new ResourceLocation(id.getNamespace(), id.getPath() + "_from_honeycomb"));
        });

        Coopperative.WEATHERING_BLOCKS.get().forEach((unweathered, weathered) -> {
            var id = Registry.BLOCK.getKey(weathered);
            ShapelessRecipeBuilder.shapeless(weathered)
                    .requires(unweathered)
                    .requires(CItems.PATINA.get())
                    .unlockedBy("has_unweathered", has(unweathered))
                    .unlockedBy("has_patina", has(CItems.PATINA.get()))
                    .save(consumer, new ResourceLocation(id.getNamespace(), id.getPath() + "_from_patina"));
        });
    }
}
