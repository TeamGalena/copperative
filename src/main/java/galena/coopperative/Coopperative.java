package galena.coopperative;

import galena.coopperative.client.CoopperativeClient;
import galena.coopperative.client.DynamicCooperativeDataPack;
import galena.coopperative.config.CommonConfig;
import galena.coopperative.config.OverwriteEnabledCondition;
import galena.coopperative.data.*;
import galena.coopperative.index.CBlocks;
import galena.coopperative.index.CItems;
import galena.coopperative.index.CLootInjects;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Coopperative.MOD_ID)
public class Coopperative {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "coopperative";
    public static final String MOD_NAME = "Coopperative";

    public Coopperative() {
        CommonConfig.register();

        DynamicCooperativeDataPack.INSTANCE.register();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CoopperativeClient::registerDynamicResources);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::gatherData);

        CraftingHelper.register(new OverwriteEnabledCondition.Serializer());

        DeferredRegister<?>[] registers = {
                CBlocks.BLOCKS,
                CBlocks.BLOCK_ENTITIES,
                CItems.ITEMS,
                CLootInjects.LOOT_CONDITIONS,
                CLootInjects.LOOT_MODIFIERS,
                //CSoundEvents.SOUNDS,
        };

        for (DeferredRegister<?> register : registers) {
            register.register(modEventBus);
        }
    }

    private void setup(FMLCommonSetupEvent event) {
    }

    private void clientSetup(FMLClientSetupEvent event) {
        CoopperativeClient.registerBlockRenderers();
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeClient()) {
            generator.addProvider(true, new CBlockStates(generator, helper));
            generator.addProvider(true, new CBlockStateOverwrites(generator, helper));
            generator.addProvider(true, new CItemModels(generator, helper));
            generator.addProvider(true, new CItemModels.ItemModelOverrides(generator, helper));
            generator.addProvider(true, new CLang(generator));
            //generator.addProvider(true, new OSoundDefinitions(generator, helper));
        }
        if (event.includeServer()) {
            generator.addProvider(true, new CRecipes(generator));
            generator.addProvider(true, new CLoot(generator));
            CTags.register(generator, helper);
            //generator.addProvider(true, new OLootTables(generator));
            //OBlockTags blockTags = new OBlockTags(generator, helper);
            //generator.addProvider(true, blockTags);
            //generator.addProvider(true, new OItemTags(generator, blockTags, helper));
            //generator.addProvider(new OAdvancements(generator, helper));
        }
    }
}
