package galena.copperative.data;

import com.google.common.base.Preconditions;
import galena.copperative.Copperative;
import galena.copperative.client.DynamicCopperativeResourcePack;
import galena.copperative.data.provider.CItemModelProvider;
import galena.copperative.index.CBlocks;
import galena.copperative.index.CItems;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
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
        return Copperative.MOD_NAME + " Item Models";
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

        CBlocks.REPEATERS.weathered().forEach(this::blockWithItem);

        CBlocks.COMPARATORS.weathered().forEach(this::blockWithItem);

        CBlocks.PISTONS.weathered().forEach(this::piston);

        CBlocks.STICKY_PISTONS.weathered().forEach(this::piston);

        CBlocks.DISPENSERS.weathered().forEach(this::block);

        CBlocks.DROPPERS.weathered().forEach(this::block);

        CBlocks.OBSERVERS.weathered().forEach(this::block);

        CBlocks.LEVERS.weathered().forEach(this::normalItem);

        CBlocks.POWERED_RAILS.weathered().forEach(this::blockFlat);

        CBlocks.EXPOSERS.weathered().map(Supplier::get).forEach(it -> compatBlock(it, "oreganized", weatherPrefix(it) + "exposer_level_0_south"));
        CBlocks.RELAYERS.weathered().map(Supplier::get).forEach(it -> compatBlock(it, "supplementaries", weatherPrefix(it) + "relayer_off"));
        CBlocks.CRANKS.weathered().map(Supplier::get).forEach(this::crank);
        CBlocks.COG_BLOCKS.weathered().map(Supplier::get).forEach(this::cogBlock);
        CBlocks.RANDOMIZERS.weathered().map(Supplier::get).forEach(it -> compatItem(it, "quark", weatherPrefix(it) + "randomizer"));
    }

    public static class ItemModelOverrides extends CItemModelProvider {
        public ItemModelOverrides(DataGenerator gen, ExistingFileHelper help) {
            super(gen, DynamicCopperativeResourcePack.NAMESPACE, help);
        }

        @Override
        public String getName() {
            return Copperative.MOD_NAME + " Item Model Overrides";
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

            CBlocks.EXPOSERS.unaffected().map(Supplier::get).ifPresent(it -> compatBlock(it, "oreganized", "exposer_level_0_south"));
            CBlocks.RELAYERS.unaffected().map(Supplier::get).ifPresent(it -> compatBlock(it, "supplementaries", "relayer_off"));
            CBlocks.CRANKS.unaffected().map(Supplier::get).ifPresent(this::crank);
            CBlocks.RANDOMIZERS.unaffected().map(Supplier::get).ifPresent(it -> compatItem(it, "quark", "randomizer"));
            compatItem(ModRegistry.DISPENSER_MINECART_ITEM.get(), "supplementaries", "dispenser_minecart");
        }

        public ItemModelBuilder block(Block block) {
            var name = Preconditions.checkNotNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
            return withExistingParent(name, blockTexture(name));
        }

        @Override
        protected ResourceLocation blockTexture(String name) {
            return new ResourceLocation(Copperative.MOD_ID, "block/" + name);
        }

        @Override
        protected ResourceLocation itemTexture(String name) {
            return new ResourceLocation(Copperative.MOD_ID, "item/" + name);
        }
    }
}
