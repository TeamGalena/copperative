package galena.coopperative.index;

import com.google.common.collect.ImmutableBiMap;
import galena.coopperative.Coopperative;
import galena.coopperative.content.block.*;
import galena.coopperative.content.block.weatheringvanilla.WeatheringLeverBlock;
import galena.coopperative.content.block.weatheringvanilla.WeatheringObserverBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class CBlocks {

    public static final CreativeModeTab REDSTONE = CreativeModeTab.TAB_REDSTONE;
    public static final CreativeModeTab BUILDING = CreativeModeTab.TAB_BUILDING_BLOCKS;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Coopperative.MOD_ID);

    public static ImmutableBiMap<Block, Block> WAXED_BLOCKS;

    public static final RegistryObject<Block> SPOT_LIGHT = register("spot_light", () -> new SpotLightBlock(BlockBehaviour.Properties.copy(Blocks.AIR).lightLevel(SpotLightBlock.LIGHT_EMISSION)));

    // Storage Blocks
    public static final RegistryObject<Block> PATINA_BLOCK = register("patina_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).sound(SoundType.TUFF)), BUILDING);

    // Decorative Blocks
    public static final ArrayList<RegistryObject<WeatheringCopperFullBlock>> COPPER_BRICKS = registerWeatheringSet("copper_bricks", weatherState -> new WeatheringCopperFullBlock(weatherState, BlockBehaviour.Properties.copy(Blocks.CUT_COPPER)), BUILDING);
    public static final ArrayList<RegistryObject<WeatheringPillarBlock>> COPPER_PILLAR = registerWeatheringSet("copper_pillar", weatherState -> new WeatheringPillarBlock(weatherState, BlockBehaviour.Properties.copy(Blocks.CUT_COPPER)), BUILDING);
    public static final ArrayList<RegistryObject<WeatheringPillarBlock>> COPPER_TILES = registerWeatheringSet("copper_tiles", weatherState -> new WeatheringPillarBlock(weatherState, BlockBehaviour.Properties.copy(Blocks.CUT_COPPER)), BUILDING);

    // Redstone Components
    public static final RegistryObject<Block> EXPOSED_LEVER = register("exposed_lever", () -> new WeatheringLeverBlock(WeatherState.EXPOSED), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_LEVER = register("weathered_lever", () -> new WeatheringLeverBlock(WeatherState.WEATHERED), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_LEVER = register("oxidized_lever", () -> new WeatheringLeverBlock(WeatherState.OXIDIZED), REDSTONE);

    public static final RegistryObject<Block> EXPOSED_OBSERVER = register("exposed_observer", () -> new WeatheringObserverBlock(WeatherState.EXPOSED), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_OBSERVER = register("weathered_observer", () -> new WeatheringObserverBlock(WeatherState.WEATHERED), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_OBSERVER = register("oxidized_observer", () -> new WeatheringObserverBlock(WeatherState.OXIDIZED), REDSTONE);
    public static final RegistryObject<Block> WAXED_OBSERVER = register("waxed_observer", () -> new WeatheringObserverBlock(WeatherState.UNAFFECTED), REDSTONE);
    public static final RegistryObject<Block> WAXED_EXPOSED_OBSERVER = register("waxed_exposed_observer", () -> new WeatheringObserverBlock(WeatherState.EXPOSED), REDSTONE);
    public static final RegistryObject<Block> WAXED_WEATHERED_OBSERVER = register("waxed_weathered_observer", () -> new WeatheringObserverBlock(WeatherState.WEATHERED), REDSTONE);
    public static final RegistryObject<Block> WAXED_OXIDIZED_OBSERVER = register("waxed_oxidized_observer", () -> new WeatheringObserverBlock(WeatherState.OXIDIZED), REDSTONE);


    public static final RegistryObject<DoorBlock> COPPER_DOOR = register("copper_door", () -> new CopperDoorBlock(WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).sound(SoundType.COPPER)), REDSTONE);
    public static final RegistryObject<DoorBlock> EXPOSED_COPPER_DOOR = register("exposed_copper_door", () -> new CopperDoorBlock(WeatherState.EXPOSED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), REDSTONE);
    public static final RegistryObject<DoorBlock> WEATHERED_COPPER_DOOR = register("weathered_copper_door", () -> new CopperDoorBlock(WeatherState.WEATHERED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), REDSTONE);
    public static final RegistryObject<DoorBlock> OXIDIZED_COPPER_DOOR = register("oxidized_copper_door", () -> new CopperDoorBlock(WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), REDSTONE);

    public static final RegistryObject<TrapDoorBlock> COPPER_TRAPDOOR = register("copper_trapdoor", () -> new CopperTrapDoorBlock(WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).sound(SoundType.COPPER)), REDSTONE);
    public static final RegistryObject<TrapDoorBlock> EXPOSED_COPPER_TRAPDOOR = register("exposed_copper_trapdoor", () -> new CopperTrapDoorBlock(WeatherState.EXPOSED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), REDSTONE);
    public static final RegistryObject<TrapDoorBlock> WEATHERED_COPPER_TRAPDOOR = register("weathered_copper_trapdoor", () -> new CopperTrapDoorBlock(WeatherState.WEATHERED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), REDSTONE);
    public static final RegistryObject<TrapDoorBlock> OXIDIZED_COPPER_TRAPDOOR = register("oxidized_copper_trapdoor", () -> new CopperTrapDoorBlock(WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), REDSTONE);

    public static final RegistryObject<Block> HEADLIGHT = register("headlight", () -> new HeadLightBlock(WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), REDSTONE);
    public static final ArrayList<RegistryObject<TogglerBlock>> TOGGLER = registerWeatheringSet("toggler", weatherState -> new TogglerBlock(weatherState, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), REDSTONE);
    public static final RegistryObject<Block> SOLDERING_TABLE = register("soldering_table", () -> new SolderingTableBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)), REDSTONE);

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> block, CreativeModeTab tab) {
        RegistryObject<B> blocks = BLOCKS.register(name, block);
        CItems.ITEMS.register(name, () -> new BlockItem(blocks.get(), new Item.Properties().tab(tab)));
        return blocks;
    }

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> block) {
        return BLOCKS.register(name, block);
    }

    public static <B extends Block> ArrayList<RegistryObject<B>> registerWeatheringSet(String name, Function<WeatherState, B> function, CreativeModeTab tab) {
        WeatherState[] wStates = WeatherState.values();
        ArrayList<RegistryObject<B>> blocks = new ArrayList<>(4);
        for (final WeatherState weatherState : wStates) {
            String prefix = weatherState.equals(WeatherState.UNAFFECTED) ? "" : weatherState.name().toLowerCase() + "_";
            Supplier<? extends B> supplier = () -> function.apply(weatherState);
            blocks.add(register(prefix + name, supplier, tab));
        }
        return blocks;
    }
}
