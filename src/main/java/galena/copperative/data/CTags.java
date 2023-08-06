package galena.copperative.data;

import galena.copperative.Copperative;
import galena.copperative.index.CItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

import static galena.copperative.index.CBlocks.*;

public class CTags {

    public static void register(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existingFileHelper) {
        var output = generator.getPackOutput();
        var blockTags = new CBlockTags(output, lookup, existingFileHelper);
        generator.addProvider(true, blockTags);
        generator.addProvider(true, new CItemTags(output, lookup, blockTags.contentsGetter(), existingFileHelper));
    }

    private static class CItemTags extends ItemTagsProvider {

        public CItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper helper) {
            super(output, lookup, blockTags, Copperative.MOD_ID, helper);
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            copy(BlockTags.RAILS, ItemTags.RAILS);
            copy(BlockTags.DOORS, ItemTags.DOORS);
            copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);

            tag(CItems.WAX_INDICATORS).add(Items.HONEYCOMB);

            tag(CRecipes.COPPER_NUGGET).add(CItems.COPPER_NUGGET.get());
        }
    }

    private static class CBlockTags extends BlockTagsProvider {

        private CBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper helper) {
            super(output, lookup, Copperative.MOD_ID, helper);
        }

        @Override
        public String getName() {
            return Copperative.MOD_NAME + " Block Tags";
        }

        @Override
        protected void addTags(HolderLookup.Provider lookup) {
            // Vanilla
            var rails = tag(BlockTags.RAILS);
            POWERED_RAILS.weathered().forEach(it -> rails.add(it.get()));

            var doors = tag(BlockTags.DOORS);
            COPPER_DOORS.forEach(it -> doors.add(it.get()));
            WAXED_COPPER_DOORS.forEach(it -> doors.add(it.get()));

            var trapdoors = tag(BlockTags.TRAPDOORS);
            COPPER_TRAPDOORS.forEach(it -> trapdoors.add(it.get()));
            WAXED_COPPER_TRAPDOORS.forEach(it -> trapdoors.add(it.get()));

            // Mineables
            var pickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);

            pickaxe.add(PATINA_BLOCK.get());

            COPPER_BRICKS.forEach(it -> pickaxe.add(it.get()));
            COPPER_TILES.forEach(it -> pickaxe.add(it.get()));
            COPPER_PILLAR.forEach(it -> pickaxe.add(it.get()));
            WAXED_COPPER_BRICKS.forEach(it -> pickaxe.add(it.get()));
            WAXED_COPPER_TILES.forEach(it -> pickaxe.add(it.get()));
            WAXED_COPPER_PILLAR.forEach(it -> pickaxe.add(it.get()));

            REPEATERS.all().forEach(it -> pickaxe.add(it.get()));
            COMPARATORS.all().forEach(it -> pickaxe.add(it.get()));
            OBSERVERS.all().forEach(it -> pickaxe.add(it.get()));
            DISPENSERS.all().forEach(it -> pickaxe.add(it.get()));
            DROPPERS.all().forEach(it -> pickaxe.add(it.get()));
            PISTONS.all().forEach(it -> pickaxe.add(it.get()));
            STICKY_PISTONS.all().forEach(it -> pickaxe.add(it.get()));
            POWERED_RAILS.all().forEach(it -> pickaxe.add(it.get()));

            HEADLIGHT.forEach(it -> pickaxe.add(it.get()));
            TOGGLER.forEach(it -> pickaxe.add(it.get()));

            COPPER_DOORS.forEach(it -> pickaxe.add(it.get()));
            COPPER_TRAPDOORS.forEach(it -> pickaxe.add(it.get()));
            WAXED_COPPER_DOORS.forEach(it -> pickaxe.add(it.get()));
            WAXED_COPPER_TRAPDOORS.forEach(it -> pickaxe.add(it.get()));

            var compatBlocks = Stream.of(
                    EXPOSERS.all(),
                    RELAYERS.all(),
                    CRANKS.all(),
                    COG_BLOCKS.all(),
                    RANDOMIZERS.all()
            ).flatMap(Function.identity());

            compatBlocks
                    .map(it -> ForgeRegistries.BLOCKS.getKey(it.get()))
                    .forEach(pickaxe::addOptional);
        }

    }


}
