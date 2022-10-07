package galena.coopperative.data;

import galena.coopperative.Coopperative;
import galena.coopperative.index.CBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperFullBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static galena.coopperative.index.CBlocks.*;

public class CBlockTags extends BlockTagsProvider {

    public CBlockTags(DataGenerator generator, @Nullable ExistingFileHelper helper) {
        super(generator, Coopperative.MOD_ID, helper);
    }

    @Override
    public String getName() {
        return Coopperative.MOD_NAME + " Block Tags";
    }

    @Override
    protected void addTags() {
        // Vanilla
        tag(BlockTags.RAILS).add(
                EXPOSED_POWERED_RAIL.get(),
                WEATHERED_POWERED_RAIL.get(),
                OXIDIZED_POWERED_RAIL.get()
        );
        // Forge

        // Mineables
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                PATINA_BLOCK.get(),

                COPPER_BRICKS.get(0).get(),
                COPPER_BRICKS.get(1).get(),
                COPPER_BRICKS.get(2).get(),
                COPPER_BRICKS.get(3).get(),

                COPPER_PILLAR.get(0).get(),
                COPPER_PILLAR.get(1).get(),
                COPPER_PILLAR.get(2).get(),
                COPPER_PILLAR.get(3).get(),

                COPPER_TILES.get(0).get(),
                COPPER_TILES.get(1).get(),
                COPPER_TILES.get(2).get(),
                COPPER_TILES.get(3).get(),

                EXPOSED_REPEATER.get(),
                WEATHERED_REPEATER.get(),
                OXIDIZED_REPEATER.get(),

                EXPOSED_COMPARATOR.get(),
                WEATHERED_COMPARATOR.get(),
                OXIDIZED_COMPARATOR.get()


        );
    }


}
