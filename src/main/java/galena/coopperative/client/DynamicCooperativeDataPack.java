package galena.coopperative.client;

import com.google.common.base.Preconditions;
import galena.coopperative.Coopperative;
import galena.coopperative.config.CommonConfig;
import galena.coopperative.data.CRecipes;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DynamicCooperativeDataPack extends DynServerResourcesProvider {

    public static final DynamicCooperativeDataPack INSTANCE = new DynamicCooperativeDataPack();

    private final Map<ResourceLocation, FinishedRecipe> recipes = new HashMap<>();

    private void registerOverride(FinishedRecipe recipe) {
        recipes.put(recipe.getId(), recipe);
    }

    private DynamicCooperativeDataPack() {
        super(new DynamicDataPack(new ResourceLocation(Coopperative.MOD_ID, "generated"), Pack.Position.BOTTOM, true, true));
        DynamicCooperativeResourcePack.OVERRIDDEN_NAMESPACES.forEach(dynamicPack::addNamespaces);
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
        if (recipes.isEmpty()) {
            CRecipes.registerOverrides(this::registerOverride);
        }

        CommonConfig.getOverwrittenBlocks(CommonConfig.OverrideTarget.RECIPE).forEach(this::enableBlockOverwrite);
    }

    private void enableBlockOverwrite(Block block) {
        var id = Preconditions.checkNotNull(ForgeRegistries.BLOCKS.getKey(block));
        var recipe = recipes.get(id);
        if (recipe != null) dynamicPack.addRecipe(recipe);
    }

}
