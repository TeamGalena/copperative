package galena.coopperative.data;

import galena.coopperative.Coopperative;
import galena.coopperative.index.CBlocks;
import galena.coopperative.data.provider.CItemModelProvider;
import galena.coopperative.index.CItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class CItemModels extends CItemModelProvider {

    public CItemModels(DataGenerator gen, ExistingFileHelper help) {
        super(gen, help);
    }

    @Override
    public @NotNull String getName() {
        return Coopperative.MOD_NAME + " Item Models";
    }

    @Override
    protected void registerModels() {
        normalItem(CItems.COPPER_NUGGET);
        normalItem(CItems.PATINA);

        block(CBlocks.PATINA_BLOCK);
        weatheringBlock(CBlocks.COPPER_BRICKS);
        weatheringBlock(CBlocks.COPPER_PILLAR);
        weatheringBlock(CBlocks.COPPER_TILES);
        //block(CBlocks.HEADLIGHT);

        blockWithItem(CBlocks.COPPER_DOOR);
        blockWithItem(CBlocks.EXPOSED_COPPER_DOOR);
        blockWithItem(CBlocks.WEATHERED_COPPER_DOOR);
        blockWithItem(CBlocks.OXIDIZED_COPPER_DOOR);

        weathingBlockWithItem(CBlocks.TOGGLER);

        trapDoor(CBlocks.COPPER_TRAPDOOR);
        trapDoor(CBlocks.EXPOSED_COPPER_TRAPDOOR);
        trapDoor(CBlocks.WEATHERED_COPPER_TRAPDOOR);
        trapDoor(CBlocks.OXIDIZED_COPPER_TRAPDOOR);

        block(CBlocks.EXPOSED_OBSERVER);
        block(CBlocks.WEATHERED_OBSERVER);
        block(CBlocks.OXIDIZED_OBSERVER);
        block(CBlocks.WAXED_OBSERVER, () -> Blocks.OBSERVER);
        block(CBlocks.WAXED_EXPOSED_OBSERVER, CBlocks.EXPOSED_OBSERVER);
        block(CBlocks.WAXED_WEATHERED_OBSERVER, CBlocks.WEATHERED_OBSERVER);
        block(CBlocks.WAXED_OXIDIZED_OBSERVER, CBlocks.OXIDIZED_OBSERVER);
    }

    public static class ItemModelOverrides extends ItemModelProvider {
        public ItemModelOverrides(DataGenerator gen, ExistingFileHelper help) {
            super(gen, "minecraft", help);
        }

        @Override
        public String getName() {
            return Coopperative.MOD_NAME + " Item Model Overrides";
        }

        @Override
        protected void registerModels() {
            block(Blocks.OBSERVER);
        }

        public ItemModelBuilder block(Block block) {
            String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
            return withExistingParent(name, Coopperative.MOD_ID + ":block/" + name);
        }
    }
}
