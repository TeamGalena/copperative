package galena.coopperative.data;

import com.google.common.base.Preconditions;
import galena.coopperative.Coopperative;
import galena.coopperative.client.DynamicCooperativeResourcePack;
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

import java.util.function.Supplier;

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
        weatheringBlock(CBlocks.WAXED_COPPER_BRICKS);
        weatheringBlock(CBlocks.WAXED_COPPER_PILLAR);
        weatheringBlock(CBlocks.WAXED_COPPER_TILES);
        weatheringBlock(CBlocks.HEADLIGHT);

        weathingBlockWithItem(CBlocks.TOGGLER);

        CBlocks.COPPER_DOORS.forEach(this::blockWithItem);
        CBlocks.COPPER_TRAPDOORS.forEach(this::trapDoor);
        CBlocks.WAXED_COPPER_DOORS.forEach(it -> blockWithItem(it, unwaxedBlockName(it)));
        CBlocks.WAXED_COPPER_TRAPDOORS.forEach(it -> trapDoor(it, unwaxedBlockName(it)));

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

        normalItem(CBlocks.EXPOSED_LEVER);
        normalItem(CBlocks.WEATHERED_LEVER);
        normalItem(CBlocks.OXIDIZED_LEVER);

        blockFlat(CBlocks.EXPOSED_POWERED_RAIL);
        blockFlat(CBlocks.WEATHERED_POWERED_RAIL);
        blockFlat(CBlocks.OXIDIZED_POWERED_RAIL);

        CBlocks.EXPOSERS.get().map(Supplier::get).forEach(it -> compatBlock(it, "oreganized", weatherPrefix(it) + "exposer_level_0_south"));
        CBlocks.RELAYERS.get().map(Supplier::get).forEach(it -> compatBlock(it, "supplementaries", weatherPrefix(it) + "relayer_off"));
    }

    public static class ItemModelOverrides extends CItemModelProvider {
        public ItemModelOverrides(DataGenerator gen, ExistingFileHelper help) {
            super(gen, DynamicCooperativeResourcePack.NAMESPACE, help);
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
            piston(() -> Blocks.STICKY_PISTON);
            block(Blocks.DISPENSER);
            block(Blocks.DROPPER);
            block(Blocks.OBSERVER);
            normalItem(() -> Items.LEVER);
            normalItem(() -> Items.REPEATER);
            normalItem(() -> Items.COMPARATOR);

            CBlocks.EXPOSERS.get().map(Supplier::get).findFirst().ifPresent(it -> compatBlock(it, "oreganized", weatherPrefix(it) + "exposer_level_0_south"));
            CBlocks.RELAYERS.get().map(Supplier::get).findFirst().ifPresent(it -> compatBlock(it, "supplementaries", weatherPrefix(it) + "relayer_off"));
        }

        public ItemModelBuilder block(Block block) {
            var name = Preconditions.checkNotNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
            return withExistingParent(name, blockTexture(name));
        }

        @Override
        protected ResourceLocation blockTexture(String name) {
            return new ResourceLocation(Coopperative.MOD_ID, "block/" + name);
        }

        @Override
        protected ResourceLocation itemTexture(String name) {
            return new ResourceLocation(Coopperative.MOD_ID, "item/" + name);
        }
    }
}
