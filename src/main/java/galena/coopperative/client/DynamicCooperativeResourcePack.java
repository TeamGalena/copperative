package galena.coopperative.client;

import com.google.common.base.Preconditions;
import galena.coopperative.Coopperative;
import galena.coopperative.config.CommonConfig;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.stream.Stream;

public class DynamicCooperativeResourcePack extends DynClientResourcesProvider {

    public static final String NAMESPACE = "overrides";

    public DynamicCooperativeResourcePack() {
        super(new DynamicTexturePack(new ResourceLocation(Coopperative.MOD_ID, "generated")));
    }

    @Override
    public Logger getLogger() {
        return Coopperative.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {
        CommonConfig.getOverwrittenBlocks().forEach(block -> enableBlockOverwrite(manager, block));
    }

    private void enableBlockOverwrite(ResourceManager manager, Block block) {
        var id = Preconditions.checkNotNull(ForgeRegistries.BLOCKS.getKey(block));
        var key = new ResourceLocation(NAMESPACE, id.getPath());

        Stream.of(ResType.BLOCKSTATES, ResType.ITEM_MODELS).forEach(type -> {
            var override = StaticResource.getOrFail(manager, type.getPath(key));
            dynamicPack.addBytes(id, override.data, type);
        });
    }

}
