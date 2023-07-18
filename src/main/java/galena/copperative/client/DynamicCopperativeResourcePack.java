package galena.copperative.client;

import com.google.common.base.Preconditions;
import galena.copperative.Copperative;
import galena.copperative.config.CommonConfig;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class DynamicCopperativeResourcePack extends DynClientResourcesProvider {

    public static final String NAMESPACE = "overrides";

    public static final List<String> OVERRIDDEN_NAMESPACES = List.of("minecraft", "oreganized", "supplementaries", "quark");

    public static final DynamicCopperativeResourcePack INSTANCE = new DynamicCopperativeResourcePack();

    private DynamicCopperativeResourcePack() {
        super(new DynamicTexturePack(new ResourceLocation(Copperative.MOD_ID, "generated"), Pack.Position.TOP, true, true));
        OVERRIDDEN_NAMESPACES.forEach(dynamicPack::addNamespaces);
    }

    @Override
    public Logger getLogger() {
        return Copperative.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    private Stream<Block> getOverwrittenBlocks() {
        return CommonConfig.getOverwrittenBlocks(CommonConfig.OverrideTarget.APPEARANCE);
    }

    private Stream<ItemLike> getOverwrittenItems() {
        var overrideDispenser = CommonConfig.isOverwriteEnabled(Blocks.DISPENSER, CommonConfig.OverrideTarget.APPEARANCE);
        Optional<ItemLike> dispenserMinecart = overrideDispenser && ModList.get().isLoaded("supplementaries")
                ? Optional.of(ModRegistry.DISPENSER_MINECART_ITEM.get())
                : Optional.empty();

        return Stream.of(
                getOverwrittenBlocks(),
                dispenserMinecart.stream()
        ).flatMap(Function.identity());
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {
        getOverwrittenBlocks().forEach(block -> enableBlockOverwrite(manager, block));
        getOverwrittenItems().forEach(block -> enableItemOverwrite(manager, block));
    }

    private void enableBlockOverwrite(ResourceManager manager, Block block) {
        var id = Preconditions.checkNotNull(ForgeRegistries.BLOCKS.getKey(block));
        var key = new ResourceLocation(NAMESPACE, id.getPath());

        var override = StaticResource.getOrLog(manager, ResType.BLOCKSTATES.getPath(key));
        if (override != null) dynamicPack.addBytes(id, override.data, ResType.BLOCKSTATES);
    }

    private void enableItemOverwrite(ResourceManager manager, ItemLike item) {
        var id = Preconditions.checkNotNull(ForgeRegistries.ITEMS.getKey(item.asItem()));
        var key = new ResourceLocation(NAMESPACE, id.getPath());

        var override = StaticResource.getOrLog(manager, ResType.ITEM_MODELS.getPath(key));
        if (override != null) dynamicPack.addBytes(id, override.data, ResType.ITEM_MODELS);
    }

}
