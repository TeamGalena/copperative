package galena.coopperative;

import com.google.common.collect.BiMap;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import galena.coopperative.client.CoopperativeClient;
import galena.coopperative.data.*;
import galena.coopperative.registry.*;

import java.util.function.Supplier;

@Mod(Coopperative.MOD_ID)
public class Coopperative {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "coopperative";

    public Coopperative() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::gatherData);



        DeferredRegister<?>[] registers = {
                //CBlockEntities.BLOCK_ENTITIES,
                CBlocks.BLOCKS,
                CItems.ITEMS,
                //CSoundEvents.SOUNDS,
        };

        for (DeferredRegister<?> register : registers) {
            register.register(modEventBus);
        }
    }

    private void setup(FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {

        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        //OreganizedClient.registerBlockRenderers();
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if(event.includeClient()) {
            generator.addProvider(true, new CBlockStates(generator, helper));
            generator.addProvider(true, new CItemModels(generator, helper));
            generator.addProvider(true, new CLang(generator));
            //generator.addProvider(true, new OSoundDefinitions(generator, helper));
        }
        if(event.includeServer()) {
            generator.addProvider(true, new CRecipes(generator));
            //generator.addProvider(true, new OLootTables(generator));
            //OBlockTags blockTags = new OBlockTags(generator, helper);
            //generator.addProvider(true, blockTags);
            //generator.addProvider(true, new OItemTags(generator, blockTags, helper));
            //generator.addProvider(new OAdvancements(generator, helper));
        }
    }
}
