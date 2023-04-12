package galena.coopperative;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import galena.coopperative.client.CoopperativeClient;
import galena.coopperative.data.*;
import galena.coopperative.index.CBlocks;
import galena.coopperative.index.CItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod(Coopperative.MOD_ID)
public class Coopperative {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "coopperative";
    public static final String MOD_NAME = "Coopperative";

    public static final List<Block> CopperizedBlocks = List.of(
            Blocks.OBSERVER, Blocks.DISPENSER, Blocks.DROPPER, Blocks.PISTON, Blocks.STICKY_PISTON,
            Blocks.REPEATER, Blocks.COMPARATOR, Blocks.LEVER, Blocks.POWERED_RAIL
    );

    public static Supplier<BiMap<Block, Block>> WEATHERING_BLOCKS;

    public Coopperative() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
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

        CBlocks.WAXED_BLOCKS = ImmutableBiMap.of();

        WEATHERING_BLOCKS = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
                .putAll(blockMapFromArray(CBlocks.COPPER_BRICKS))
                .putAll(blockMapFromArray(CBlocks.COPPER_PILLAR))
                .putAll(blockMapFromArray(CBlocks.COPPER_TILES))
                .putAll(blockMapFromArray(CBlocks.TOGGLER))
                .putAll(blockMapFromArray(CBlocks.HEADLIGHT))

                .put(Blocks.REPEATER, CBlocks.EXPOSED_REPEATER.get())
                .put(CBlocks.EXPOSED_REPEATER.get(), CBlocks.WEATHERED_REPEATER.get())
                .put(CBlocks.WEATHERED_REPEATER.get(), CBlocks.OXIDIZED_REPEATER.get())

                .put(Blocks.COMPARATOR, CBlocks.EXPOSED_COMPARATOR.get())
                .put(CBlocks.EXPOSED_COMPARATOR.get(), CBlocks.WEATHERED_COMPARATOR.get())
                .put(CBlocks.WEATHERED_COMPARATOR.get(), CBlocks.OXIDIZED_COMPARATOR.get())

                .put(Blocks.PISTON, CBlocks.EXPOSED_PISTON.get())
                .put(CBlocks.EXPOSED_PISTON.get(), CBlocks.WEATHERED_PISTON.get())
                .put(CBlocks.WEATHERED_PISTON.get(), CBlocks.OXIDIZED_PISTON.get())

                .put(Blocks.STICKY_PISTON, CBlocks.EXPOSED_STICKY_PISTON.get())
                .put(CBlocks.EXPOSED_STICKY_PISTON.get(), CBlocks.WEATHERED_STICKY_PISTON.get())
                .put(CBlocks.WEATHERED_STICKY_PISTON.get(), CBlocks.OXIDIZED_STICKY_PISTON.get())

                .put(Blocks.OBSERVER, CBlocks.EXPOSED_OBSERVER.get())
                .put(CBlocks.EXPOSED_OBSERVER.get(), CBlocks.WEATHERED_OBSERVER.get())
                .put(CBlocks.WEATHERED_OBSERVER.get(), CBlocks.OXIDIZED_OBSERVER.get())

                .put(Blocks.DISPENSER, CBlocks.EXPOSED_DISPENSER.get())
                .put(CBlocks.EXPOSED_DISPENSER.get(), CBlocks.WEATHERED_DISPENSER.get())
                .put(CBlocks.WEATHERED_DISPENSER.get(), CBlocks.OXIDIZED_DISPENSER.get())

                .put(Blocks.DROPPER, CBlocks.EXPOSED_DROPPER.get())
                .put(CBlocks.EXPOSED_DROPPER.get(), CBlocks.WEATHERED_DROPPER.get())
                .put(CBlocks.WEATHERED_DROPPER.get(), CBlocks.OXIDIZED_DROPPER.get())

                .put(Blocks.LEVER, CBlocks.EXPOSED_LEVER.get())
                .put(CBlocks.EXPOSED_LEVER.get(), CBlocks.WEATHERED_LEVER.get())
                .put(CBlocks.WEATHERED_LEVER.get(), CBlocks.OXIDIZED_LEVER.get())

                .put(Blocks.POWERED_RAIL, CBlocks.EXPOSED_POWERED_RAIL.get())
                .put(CBlocks.EXPOSED_POWERED_RAIL.get(), CBlocks.WEATHERED_POWERED_RAIL.get())
                .put(CBlocks.WEATHERED_POWERED_RAIL.get(), CBlocks.OXIDIZED_POWERED_RAIL.get())

                .put(CBlocks.COPPER_DOOR.get(), CBlocks.EXPOSED_COPPER_DOOR.get())
                .put(CBlocks.EXPOSED_COPPER_DOOR.get(), CBlocks.WEATHERED_COPPER_DOOR.get())
                .put(CBlocks.WEATHERED_COPPER_DOOR.get(), CBlocks.OXIDIZED_COPPER_DOOR.get())

                .put(CBlocks.COPPER_TRAPDOOR.get(), CBlocks.EXPOSED_COPPER_TRAPDOOR.get())
                .put(CBlocks.EXPOSED_COPPER_TRAPDOOR.get(), CBlocks.WEATHERED_COPPER_TRAPDOOR.get())
                .put(CBlocks.WEATHERED_COPPER_TRAPDOOR.get(), CBlocks.OXIDIZED_COPPER_TRAPDOOR.get())
                .build());
    }

    private void clientSetup(FMLClientSetupEvent event) {
        CoopperativeClient.registerBlockRenderers();
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if(event.includeClient()) {
            generator.addProvider(true, new CBlockStates(generator, helper));
            generator.addProvider(true, new CItemModels(generator, helper));
            generator.addProvider(true, new CItemModels.ItemModelOverrides(generator, helper));
            generator.addProvider(true, new CLang(generator));
            //generator.addProvider(true, new OSoundDefinitions(generator, helper));
        }
        if(event.includeServer()) {
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

    private <B extends Block> ImmutableBiMap<Block, Block> blockMapFromArray(ArrayList<RegistryObject<B>> blockArrayList) {
        ImmutableBiMap.Builder<Block, Block> map = new ImmutableBiMap.Builder<>();
        for (int i = 0; blockArrayList.size() - 1 > i; i++)
            map.put(blockArrayList.get(i).get(), blockArrayList.get(i + 1).get());

        return map.build();
    }
}
