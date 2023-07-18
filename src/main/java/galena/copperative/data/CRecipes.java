package galena.copperative.data;

import galena.copperative.config.CommonConfig;
import galena.copperative.config.OverwriteEnabledCondition;
import galena.copperative.data.provider.CRecipeProvider;
import galena.copperative.index.CBlocks;
import galena.copperative.index.CConversions;
import galena.copperative.index.CItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static galena.copperative.Copperative.MOD_ID;

public class CRecipes extends CRecipeProvider {

    protected static final TagKey<Item> COPPER_INGOT = Tags.Items.INGOTS_COPPER;
    public static final TagKey<Item> COPPER_NUGGET = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "nuggets/copper"));
    protected static final TagKey<Item> REDSTONE_DUST = Tags.Items.DUSTS_REDSTONE;

    protected static final TagKey<Item> SILVER_INGOT = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "ingots/silver"));
    protected static final Item REDSTONE_TORCH = Items.REDSTONE_TORCH;

    public CRecipes(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

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
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CBlocks.COPPER_PILLAR.get(i).get(), 2)
                    .pattern("A")
                    .pattern("A")
                    .define('A', cutCopper[i])
                    .unlockedBy("has_cut_copper", has(cutCopper[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CBlocks.WAXED_COPPER_PILLAR.get(i).get(), 2)
                    .pattern("A")
                    .pattern("A")
                    .define('A', cutWaxedCopper[i])
                    .unlockedBy("has_cut_copper", has(cutWaxedCopper[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CBlocks.COPPER_BRICKS.get(i).get(), 4)
                    .pattern("AA")
                    .pattern("AA")
                    .define('A', cutCopper[i])
                    .unlockedBy("has_cut_copper", has(cutCopper[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CBlocks.WAXED_COPPER_BRICKS.get(i).get(), 4)
                    .pattern("AA")
                    .pattern("AA")
                    .define('A', cutWaxedCopper[i])
                    .unlockedBy("has_cut_copper", has(cutWaxedCopper[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CBlocks.COPPER_TILES.get(i).get())
                    .pattern("A")
                    .pattern("A")
                    .define('A', cutCopperSlabs[i])
                    .unlockedBy("has_cut_copper", has(cutCopperSlabs[i]))
                    .save(consumer);

            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CBlocks.WAXED_COPPER_TILES.get(i).get())
                    .pattern("A")
                    .pattern("A")
                    .define('A', cutWaxedCopperSlabs[i])
                    .unlockedBy("has_cut_copper", has(cutWaxedCopperSlabs[i]))
                    .save(consumer);
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, CBlocks.TOGGLER.get(0).get())
                .pattern(" A ")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', REDSTONE_DUST)
                .define('C', COPPER_INGOT)
                .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, CBlocks.HEADLIGHT.get(0).get())
                .pattern("CBC")
                .pattern(" A ")
                .pattern(" A ")
                .define('A', COPPER_INGOT)
                .define('B', Items.AMETHYST_SHARD)
                .define('C', Items.REDSTONE_LAMP)
                .unlockedBy("has_spyglass", has(Items.SPYGLASS))
                .unlockedBy("has_redstone_lamp", has(Items.REDSTONE_LAMP))
                .save(consumer);

        CConversions.getWaxedPairs().forEach(entry -> {
            var unwaxed = entry.getKey();
            var waxed = entry.getValue();
            var id = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(waxed));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, waxed)
                    .requires(unwaxed)
                    .requires(Items.HONEYCOMB)
                    .unlockedBy("has_unwaxed", has(unwaxed))
                    .save(consumer, new ResourceLocation(id.getNamespace(), id.getPath() + "_from_honeycomb"));
        });

        CConversions.getWeatheredPairs().forEach(entry -> {
            var unweathered = entry.getKey();
            var weathered = entry.getValue();
            var id = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(weathered));

            var recipe = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, weathered)
                    .requires(unweathered)
                    .requires(CItems.PATINA.get())
                    .unlockedBy("has_unweathered", has(unweathered))
                    .unlockedBy("has_patina", has(CItems.PATINA.get()));

            var recipeId = suffix(id, "_from_patina");
            if (CommonConfig.isPossibleOverwrite(unweathered)) {
                conditional(recipe, unweathered, recipeId, consumer);
            } else {
                recipe.save(consumer, recipeId);
            }
        });

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.PRISMARINE_SHARD)
                .pattern("AA ")
                .pattern("ABA")
                .pattern(" AA")
                .define('A', CItems.PATINA.get())
                .define('B', Items.CLAY_BALL)
                .unlockedBy("has_patina", has(CItems.PATINA.get()))
                .group("prismarine_shard")
                .save(consumer, new ResourceLocation(MOD_ID, "prismarine_shard_from_clay"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.PRISMARINE_SHARD)
                .pattern(" AA")
                .pattern("ABA")
                .pattern("AA ")
                .define('A', CItems.PATINA.get())
                .define('B', Items.CLAY_BALL)
                .unlockedBy("has_patina", has(CItems.PATINA.get()))
                .group("prismarine_shard")
                .save(consumer, new ResourceLocation(MOD_ID, "prismarine_shard_from_clay_mirrored"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PRISMARINE_CRYSTALS)
                .requires(Items.GLOW_INK_SAC)
                .requires(CItems.PATINA.get())
                .requires(CItems.PATINA.get())
                .requires(CItems.PATINA.get())
                .unlockedBy("has_patina", has(CItems.PATINA.get()))
                .group("prismarine_crystals")
                .save(consumer, new ResourceLocation(MOD_ID, "prismarine_crystals_from_glow_ink"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PRISMARINE_CRYSTALS)
                .requires(CItems.PATINA.get())
                .requires(Items.GLOWSTONE_DUST)
                .requires(Items.GLOWSTONE_DUST)
                .requires(CItems.PATINA.get())
                .unlockedBy("has_patina", has(CItems.PATINA.get()))
                .group("prismarine_crystals")
                .save(consumer, new ResourceLocation(MOD_ID, "prismarine_crystals_from_glowstone"));
    }

    public static void registerOverrides(BiConsumer<FinishedRecipe, ItemLike> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Blocks.REPEATER)
                .pattern("ABA")
                .pattern("CCC")
                .define('A', REDSTONE_TORCH)
                .define('B', REDSTONE_DUST)
                .define('C', COPPER_INGOT)
                .unlockedBy("has_redstone_dust", has(REDSTONE_DUST))
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(it -> consumer.accept(it, Blocks.REPEATER));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Blocks.COMPARATOR)
                .pattern(" A ")
                .pattern("ABA")
                .pattern("CCC")
                .define('A', REDSTONE_TORCH)
                .define('B', Items.QUARTZ)
                .define('C', COPPER_INGOT)
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(it -> consumer.accept(it, Blocks.COMPARATOR));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Blocks.PISTON)
                .pattern("AAA")
                .pattern("BCB")
                .pattern("BDB")
                .define('A', ItemTags.PLANKS)
                .define('B', COPPER_INGOT)
                .define('C', Tags.Items.INGOTS_IRON)
                .define('D', REDSTONE_DUST)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(it -> consumer.accept(it, Blocks.PISTON));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Blocks.OBSERVER)
                .pattern("AAA")
                .pattern("BBC")
                .pattern("AAA")
                .define('A', COPPER_INGOT)
                .define('B', REDSTONE_DUST)
                .define('C', Items.QUARTZ)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(it -> consumer.accept(it, Blocks.OBSERVER));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Blocks.DISPENSER)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("ACA")
                .define('A', COPPER_INGOT)
                .define('B', Items.BOW)
                .define('C', REDSTONE_DUST)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(it -> consumer.accept(it, Blocks.DISPENSER));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Blocks.DROPPER)
                .pattern("AAA")
                .pattern("A A")
                .pattern("ABA")
                .define('A', COPPER_INGOT)
                .define('B', REDSTONE_DUST)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(it -> consumer.accept(it, Blocks.DROPPER));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Blocks.LEVER)
                .pattern("C")
                .pattern("A")
                .pattern("B")
                .define('A', Tags.Items.RODS_WOODEN)
                .define('B', COPPER_INGOT)
                .define('C', COPPER_NUGGET)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(it -> consumer.accept(it, Blocks.LEVER));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Blocks.POWERED_RAIL, 6)
                .pattern("A A")
                .pattern("ABA")
                .pattern("ACA")
                .define('A', COPPER_INGOT)
                .define('B', Tags.Items.RODS_WOODEN)
                .define('C', REDSTONE_DUST)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(it -> consumer.accept(it, Blocks.POWERED_RAIL));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.SPYGLASS)
                .pattern("BCB")
                .pattern(" A ")
                .pattern(" A ")
                .define('A', COPPER_INGOT)
                .define('B', COPPER_NUGGET)
                .define('C', Items.AMETHYST_SHARD)
                .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                .save(it -> consumer.accept(it, Items.SPYGLASS));

        CBlocks.EXPOSERS.unaffected().ifPresent(block ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, block.get())
                        .pattern("AAA")
                        .pattern("BBC")
                        .pattern("AAA")
                        .define('A', COPPER_INGOT)
                        .define('B', REDSTONE_DUST)
                        .define('C', SILVER_INGOT)
                        .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                        .save(it -> consumer.accept(it, block.get()))
        );

        CBlocks.RELAYERS.unaffected().ifPresent(block ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, block.get())
                        .pattern("AAA")
                        .pattern("BBC")
                        .pattern("AAA")
                        .define('A', COPPER_INGOT)
                        .define('B', REDSTONE_DUST)
                        .define('C', Tags.Items.INGOTS_IRON)
                        .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                        .save(it -> consumer.accept(it, block.get()))
        );

        CBlocks.CRANKS.unaffected().ifPresent(block ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, block.get())
                        .pattern(" C ")
                        .pattern(" A ")
                        .pattern("BBB")
                        .define('A', Tags.Items.RODS_WOODEN)
                        .define('B', COPPER_INGOT)
                        .define('C', COPPER_NUGGET)
                        .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                        .save(it -> consumer.accept(it, block.get()))
        );

        CBlocks.RANDOMIZERS.unaffected().ifPresent(block ->
                ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, block.get())
                        .pattern(" A ")
                        .pattern("ABA")
                        .pattern("CCC")
                        .define('A', Items.REDSTONE_TORCH)
                        .define('B', Items.PRISMARINE_CRYSTALS)
                        .define('C', COPPER_INGOT)
                        .unlockedBy("has_copper_ingot", has(COPPER_INGOT))
                        .unlockedBy("has_prismarine", has(Items.PRISMARINE_CRYSTALS))
                        .save(it -> consumer.accept(it, block.get()), new ResourceLocation("quark", "automation/crafting/redstone_randomizer"))
        );
    }

    private static ResourceLocation suffix(ResourceLocation in, String suffix) {
        return new ResourceLocation(in.getNamespace(), in.getPath() + suffix);
    }

    private static void conditional(RecipeBuilder recipe, Block target, ResourceLocation id, Consumer<FinishedRecipe> consumer) {
        ConditionalRecipe.builder()
                .addCondition(new OverwriteEnabledCondition(target, CommonConfig.OverrideTarget.WEATHERING))
                .addRecipe(recipe::save)
                .build(consumer, id);
    }
}
