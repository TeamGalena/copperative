package galena.copperative;

import galena.copperative.client.CopperativeClient;
import galena.copperative.client.DynamicCopperativeDataPack;
import galena.copperative.config.CommonConfig;
import galena.copperative.config.OverwriteEnabledCondition;
import galena.copperative.data.CBlockStateOverwrites;
import galena.copperative.data.CBlockStates;
import galena.copperative.data.CItemModels;
import galena.copperative.data.CLang;
import galena.copperative.data.CLoot;
import galena.copperative.data.CRecipes;
import galena.copperative.data.CTags;
import galena.copperative.index.CBlocks;
import galena.copperative.index.CItems;
import galena.copperative.index.CLootInjects;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Copperative.MOD_ID)
public class Copperative {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "copperative";
    public static final String MOD_NAME = "Copperative";

    public Copperative() {
        CommonConfig.register();

        DynamicCopperativeDataPack.INSTANCE.register();
        //noinspection Convert2MethodRef - passing a lambda reference crashes server due to loading of client-only classes
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CopperativeClient.register());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
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
