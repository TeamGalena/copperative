package galena.coopperative.data;

import galena.coopperative.Coopperative;
import galena.coopperative.data.provider.CItemModelProvider;
import galena.coopperative.index.CBlocks;
import galena.coopperative.index.CItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
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
        weatheringBlock(CBlocks.HEADLIGHT);

        blockWithItem(CBlocks.COPPER_DOOR);
        blockWithItem(CBlocks.EXPOSED_COPPER_DOOR);
        blockWithItem(CBlocks.WEATHERED_COPPER_DOOR);
        blockWithItem(CBlocks.OXIDIZED_COPPER_DOOR);

        weathingBlockWithItem(CBlocks.TOGGLER);

        trapDoor(CBlocks.COPPER_TRAPDOOR);
        trapDoor(CBlocks.EXPOSED_COPPER_TRAPDOOR);
        trapDoor(CBlocks.WEATHERED_COPPER_TRAPDOOR);
        trapDoor(CBlocks.OXIDIZED_COPPER_TRAPDOOR);

        blockWithItem(CBlocks.EXPOSED_REPEATER);
        blockWithItem(CBlocks.WEATHERED_REPEATER);
        blockWithItem(CBlocks.OXIDIZED_REPEATER);

        blockWithItem(CBlocks.EXPOSED_COMPARATOR);
        blockWithItem(CBlocks.WEATHERED_COMPARATOR);
        blockWithItem(CBlocks.OXIDIZED_COMPARATOR);

        piston(CBlocks.EXPOSED_PISTON);
        piston(CBlocks.WEATHERED_PISTON);
        piston(CBlocks.OXIDIZED_PISTON);

        piston(CBlocks.EXPOSED_STICKY_PISTON);
        piston(CBlocks.WEATHERED_STICKY_PISTON);
        piston(CBlocks.OXIDIZED_STICKY_PISTON);

        block(CBlocks.EXPOSED_DISPENSER);
        block(CBlocks.WEATHERED_DISPENSER);
        block(CBlocks.OXIDIZED_DISPENSER);

        block(CBlocks.EXPOSED_DROPPER);
        block(CBlocks.WEATHERED_DROPPER);
        block(CBlocks.OXIDIZED_DROPPER);

        block(CBlocks.EXPOSED_OBSERVER);
        block(CBlocks.WEATHERED_OBSERVER);
        block(CBlocks.OXIDIZED_OBSERVER);
        block(CBlocks.WAXED_OBSERVER, () -> Blocks.OBSERVER);
        block(CBlocks.WAXED_EXPOSED_OBSERVER, CBlocks.EXPOSED_OBSERVER);
        block(CBlocks.WAXED_WEATHERED_OBSERVER, CBlocks.WEATHERED_OBSERVER);
        block(CBlocks.WAXED_OXIDIZED_OBSERVER, CBlocks.OXIDIZED_OBSERVER);

        blockFlat(CBlocks.EXPOSED_LEVER);
        blockFlat(CBlocks.WEATHERED_LEVER);
        blockFlat(CBlocks.OXIDIZED_LEVER);

        blockFlat(CBlocks.EXPOSED_POWERED_RAIL);
        blockFlat(CBlocks.WEATHERED_POWERED_RAIL);
        blockFlat(CBlocks.OXIDIZED_POWERED_RAIL);
    }

    public static class ItemModelOverrides extends CItemModelProvider {
        public ItemModelOverrides(DataGenerator gen, ExistingFileHelper help) {
            super(gen, "minecraft", help);
        }

        @Override
        public String getName() {
            return Coopperative.MOD_NAME + " Item Model Overrides";
        }

        @Override
        protected void registerModels() {
            blockFlat(() -> Blocks.LEVER);
            blockFlat(() -> Blocks.POWERED_RAIL);
            piston(() -> Blocks.PISTON);
            block(Blocks.DISPENSER);
            block(Blocks.DROPPER);
            block(Blocks.OBSERVER);
            normalItem(() -> Items.LEVER);
            normalItem(() -> Items.REPEATER);
            normalItem(() -> Items.COMPARATOR);
        }

        public ItemModelBuilder block(Block block) {
            String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
            return withExistingParent(name, blockTexture(name));
        }

        @Override
        protected ResourceLocation blockTexture(String name) {
            return new ResourceLocation(Coopperative.MOD_ID + ":block/" + name);
        }

        @Override
        protected ResourceLocation itemTexture(String name) {
            return new ResourceLocation(Coopperative.MOD_ID + ":item/" + name);
        }
    }
}
