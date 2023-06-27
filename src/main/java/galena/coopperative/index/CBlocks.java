package galena.coopperative.index;

import galena.coopperative.Coopperative;
import galena.coopperative.content.block.*;
import galena.coopperative.content.block.compat.WeatheredCrank;
import galena.coopperative.content.block.compat.WeatheredExposer;
import galena.coopperative.content.block.compat.WeatheredRelayer;
import galena.coopperative.content.block.tile.HeadlightTile;
import galena.coopperative.content.block.weatheringvanilla.*;
import galena.oreganized.index.OBlocks;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import galena.coopperative.content.block.compat.WeatheredExposer;
import galena.coopperative.content.block.compat.WeatheredRandomizer;
import galena.coopperative.content.block.compat.WeatheredRelayer;
import galena.coopperative.content.block.tile.HeadlightTile;
import galena.coopperative.content.block.weatheringvanilla.*;
import galena.oreganized.index.OBlocks;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class CBlocks {

    public static final CreativeModeTab REDSTONE = CreativeModeTab.TAB_REDSTONE;
    public static final CreativeModeTab TRANSPORT = CreativeModeTab.TAB_TRANSPORTATION;
    public static final CreativeModeTab BUILDING = CreativeModeTab.TAB_BUILDING_BLOCKS;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Coopperative.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Coopperative.MOD_ID);

    public static final RegistryObject<SpotLightBlock> SPOT_LIGHT = register("spot_light", () -> new SpotLightBlock(Properties.of(Material.AIR).noCollission().noLootTable().noOcclusion().lightLevel(SpotLightBlock.LIGHT_EMISSION)));

    // Storage Blocks
    public static final RegistryObject<Block> PATINA_BLOCK = register("patina_block", () -> new SandBlock(0xBD3A0, Properties.copy(Blocks.SAND).sound(SoundType.TUFF)), BUILDING);

    // Decorative Blocks
    public static final List<RegistryObject<WeatheringCopperFullBlock>> COPPER_BRICKS = registerWeatheringSet("copper_bricks", weatherState -> new WeatheringCopperFullBlock(weatherState, Properties.copy(Blocks.CUT_COPPER)), BUILDING);
    public static final List<RegistryObject<WeatheringPillarBlock>> COPPER_PILLAR = registerWeatheringSet("copper_pillar", weatherState -> new WeatheringPillarBlock(weatherState, Properties.copy(Blocks.CUT_COPPER)), BUILDING);
    public static final List<RegistryObject<WeatheringPillarBlock>> COPPER_TILES = registerWeatheringSet("copper_tiles", weatherState -> new WeatheringPillarBlock(weatherState, Properties.copy(Blocks.CUT_COPPER)), BUILDING);

    public static final List<RegistryObject<Block>> WAXED_COPPER_BRICKS = registerWaxedSet("copper_bricks", $ -> new Block(Properties.copy(Blocks.CUT_COPPER)), BUILDING);
    public static final List<RegistryObject<RotatedPillarBlock>> WAXED_COPPER_PILLAR = registerWaxedSet("copper_pillar", $ -> new RotatedPillarBlock(Properties.copy(Blocks.CUT_COPPER)), BUILDING);
    public static final List<RegistryObject<RotatedPillarBlock>> WAXED_COPPER_TILES = registerWaxedSet("copper_tiles", $ -> new RotatedPillarBlock(Properties.copy(Blocks.CUT_COPPER)), BUILDING);

    // Redstone Components
    public static final RegistryObject<Block> EXPOSED_REPEATER = register("exposed_repeater", () -> new WeatheringRepeaterBlock(WeatherState.EXPOSED), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_REPEATER = register("weathered_repeater", () -> new WeatheringRepeaterBlock(WeatherState.WEATHERED), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_REPEATER = register("oxidized_repeater", () -> new WeatheringRepeaterBlock(WeatherState.OXIDIZED), REDSTONE);

    public static final RegistryObject<Block> EXPOSED_COMPARATOR = register("exposed_comparator", () -> new WeatheringComparatorBlock(WeatherState.EXPOSED), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_COMPARATOR = register("weathered_comparator", () -> new WeatheringComparatorBlock(WeatherState.WEATHERED), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_COMPARATOR = register("oxidized_comparator", () -> new WeatheringComparatorBlock(WeatherState.OXIDIZED), REDSTONE);

    public static final RegistryObject<Block> EXPOSED_PISTON = register("exposed_piston", () -> new WeatheringPistonBlock(WeatherState.EXPOSED, false), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_PISTON = register("weathered_piston", () -> new WeatheringPistonBlock(WeatherState.WEATHERED, false), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_PISTON = register("oxidized_piston", () -> new WeatheringPistonBlock(WeatherState.OXIDIZED, false), REDSTONE);

    public static final RegistryObject<Block> EXPOSED_STICKY_PISTON = register("exposed_sticky_piston", () -> new WeatheringPistonBlock(WeatherState.EXPOSED, true), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_STICKY_PISTON = register("weathered_sticky_piston", () -> new WeatheringPistonBlock(WeatherState.WEATHERED, true), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_STICKY_PISTON = register("oxidized_sticky_piston", () -> new WeatheringPistonBlock(WeatherState.OXIDIZED, true), REDSTONE);

    public static final RegistryObject<Block> EXPOSED_OBSERVER = register("exposed_observer", () -> new WeatheringObserverBlock(WeatherState.EXPOSED), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_OBSERVER = register("weathered_observer", () -> new WeatheringObserverBlock(WeatherState.WEATHERED), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_OBSERVER = register("oxidized_observer", () -> new WeatheringObserverBlock(WeatherState.OXIDIZED), REDSTONE);
    public static final RegistryObject<Block> EXPOSED_DISPENSER = register("exposed_dispenser", () -> new WeatheringDispenserBlock(WeatherState.EXPOSED), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_DISPENSER = register("weathered_dispenser", () -> new WeatheringDispenserBlock(WeatherState.WEATHERED), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_DISPENSER = register("oxidized_dispenser", () -> new WeatheringDispenserBlock(WeatherState.OXIDIZED), REDSTONE);

    public static final RegistryObject<Block> EXPOSED_DROPPER = register("exposed_dropper", () -> new WeatheringDropperBlock(WeatherState.EXPOSED), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_DROPPER = register("weathered_dropper", () -> new WeatheringDropperBlock(WeatherState.WEATHERED), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_DROPPER = register("oxidized_dropper", () -> new WeatheringDropperBlock(WeatherState.OXIDIZED), REDSTONE);

    public static final RegistryObject<Block> EXPOSED_LEVER = register("exposed_lever", () -> new WeatheringLeverBlock(WeatherState.EXPOSED), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_LEVER = register("weathered_lever", () -> new WeatheringLeverBlock(WeatherState.WEATHERED), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_LEVER = register("oxidized_lever", () -> new WeatheringLeverBlock(WeatherState.OXIDIZED), REDSTONE);

    public static final List<RegistryObject<DoorBlock>> COPPER_DOORS = registerWeatheringSet("copper_door", it -> new CopperDoorBlock(it, Properties.copy(Blocks.IRON_DOOR).sound(SoundType.COPPER)), REDSTONE);
    //public static final RegistryObject<DoorBlock> EXPOSED_COPPER_DOOR = register("exposed_copper_door", () -> new CopperDoorBlock(WeatherState.EXPOSED, Properties.copy(COPPER_DOOR.get())), REDSTONE);
    //public static final RegistryObject<DoorBlock> WEATHERED_COPPER_DOOR = register("weathered_copper_door", () -> new CopperDoorBlock(WeatherState.WEATHERED, Properties.copy(COPPER_DOOR.get())), REDSTONE);
    //public static final RegistryObject<DoorBlock> OXIDIZED_COPPER_DOOR = register("oxidized_copper_door", () -> new CopperDoorBlock(WeatherState.OXIDIZED, Properties.copy(COPPER_DOOR.get())), REDSTONE);

    public static final List<RegistryObject<TrapDoorBlock>> COPPER_TRAPDOORS = registerWeatheringSet("copper_trapdoor", it -> new CopperTrapDoorBlock(it, Properties.copy(Blocks.IRON_TRAPDOOR).sound(SoundType.COPPER)), REDSTONE);
    //public static final RegistryObject<TrapDoorBlock> EXPOSED_COPPER_TRAPDOOR = register("exposed_copper_trapdoor", () -> new CopperTrapDoorBlock(WeatherState.EXPOSED, Properties.copy(COPPER_DOOR.get())), REDSTONE);
    //public static final RegistryObject<TrapDoorBlock> WEATHERED_COPPER_TRAPDOOR = register("weathered_copper_trapdoor", () -> new CopperTrapDoorBlock(WeatherState.WEATHERED, Properties.copy(COPPER_DOOR.get())), REDSTONE);
    //public static final RegistryObject<TrapDoorBlock> OXIDIZED_COPPER_TRAPDOOR = register("oxidized_copper_trapdoor", () -> new CopperTrapDoorBlock(WeatherState.OXIDIZED, Properties.copy(COPPER_DOOR.get())), REDSTONE);

    public static final List<RegistryObject<DoorBlock>> WAXED_COPPER_DOORS = registerWaxedSet("copper_door", it -> new WaxedDoorBlock(Properties.copy(Blocks.IRON_DOOR).sound(SoundType.COPPER)), REDSTONE);

    public static final List<RegistryObject<TrapDoorBlock>> WAXED_COPPER_TRAPDOORS = registerWaxedSet("copper_trapdoor", $ -> new AbstractCopperTrapdoorBlock(Properties.copy(Blocks.IRON_TRAPDOOR).sound(SoundType.COPPER)), REDSTONE);


    public static final List<RegistryObject<Block>> HEADLIGHT = registerWeatheringSet("headlight", weatherState -> new HeadLightBlock(weatherState, Properties.copy(Blocks.COPPER_BLOCK).lightLevel(HeadLightBlock.LIGHT_EMISSION)), REDSTONE);
    public static final RegistryObject<BlockEntityType<HeadlightTile>> HEADLIGHT_TILE = BLOCK_ENTITIES.register("headlight", () -> {
        var blocks = HEADLIGHT.stream().map(RegistryObject::get).toArray(Block[]::new);
        return BlockEntityType.Builder.of(HeadlightTile::new, blocks).build(null);
    });

    public static final List<RegistryObject<Block>> TOGGLER = registerWeatheringSet("toggler", weatherState -> new TogglerBlock(weatherState, Properties.copy(Blocks.COPPER_BLOCK)), REDSTONE);

    // Rails
    public static final RegistryObject<Block> EXPOSED_POWERED_RAIL = register("exposed_powered_rail", () -> new WeatheringPoweredRailBlock(WeatherState.EXPOSED), TRANSPORT);
    public static final RegistryObject<Block> WEATHERED_POWERED_RAIL = register("weathered_powered_rail", () -> new WeatheringPoweredRailBlock(WeatherState.WEATHERED), TRANSPORT);
    public static final RegistryObject<Block> OXIDIZED_POWERED_RAIL = register("oxidized_powered_rail", () -> new WeatheringPoweredRailBlock(WeatherState.OXIDIZED), TRANSPORT);

    // Workstations
    //public static final RegistryObject<Block> SOLDERING_TABLE = register("soldering_table", () -> new SolderingTableBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)), REDSTONE);

    // Compat
    public static final CopperSet<Block> EXPOSERS = ifLoaded("oreganized",
            () -> registerConvertedSet("exposer", OBlocks.EXPOSER, WeatheredExposer::new, CreativeModeTab.TAB_REDSTONE), CopperSet::empty
    );

    public static final CopperSet<Block> RELAYERS = ifLoaded("supplementaries",
            () -> registerConvertedSet("relayer", ModRegistry.RELAYER, WeatheredRelayer::new, CreativeModeTab.TAB_REDSTONE), CopperSet::empty
    );

    public static final CopperSet<Block> CRANKS = ifLoaded("supplementaries",
            () -> registerConvertedSet("crank", ModRegistry.CRANK, WeatheredCrank::new, CreativeModeTab.TAB_REDSTONE), CopperSet::empty
    );

    public static final CopperSet<Block> RANDOMIZERS = ifLoaded("quark",
            () -> registerConvertedSet("randomizer", ModRegistry.CRANK, WeatheredRandomizer::new, CreativeModeTab.TAB_REDSTONE), CopperSet::empty
    );

    public static final Supplier<Stream<Supplier<Block>>> RELAYERS = ifLoaded("supplementaries",
            () -> registerConvertedSet("relayer", ModRegistry.RELAYER, WeatheredRelayer::new, CreativeModeTab.TAB_REDSTONE)::stream
    );


    public static final Supplier<Stream<Supplier<Block>>> RANDOMIZERS = ifLoaded("quark",
            () -> registerConvertedSet("randomizer", WeatheredRandomizer::loadUnaffected, WeatheredRandomizer::new, CreativeModeTab.TAB_REDSTONE)::stream
    );

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> block, CreativeModeTab tab) {
        RegistryObject<B> blocks = BLOCKS.register(name, block);
        CItems.ITEMS.register(name, () -> new BlockItem(blocks.get(), new Item.Properties().tab(tab)));
        return blocks;
    }

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> block) {
        return BLOCKS.register(name, block);
    }

    public static <B extends Block> CopperSet<B> registerConvertedSet(String name, Supplier<B> targetSupplier, Function<WeatherState, B> function, CreativeModeTab tab) {
        var weathered = Stream.of(WeatherState.EXPOSED, WeatherState.WEATHERED, WeatherState.OXIDIZED).<Supplier<B>>map(weatherState -> {
            String prefix = weatherState.name().toLowerCase() + "_";
            return register(prefix + name, () -> function.apply(weatherState), tab);
        }).toList();
        return new CopperSet<>(targetSupplier, weathered);
    }

    public static <R> R ifLoaded(String mod, Supplier<R> supplier, Supplier<R> emptySupplier) {
        if (ModList.get().isLoaded(mod)) return supplier.get();
        else return emptySupplier.get();
    }

    public static <B extends Block> List<RegistryObject<B>> registerWeatheringSet(UnaryOperator<String> name, Function<WeatherState, B> function, CreativeModeTab tab) {
        WeatherState[] wStates = WeatherState.values();
        ArrayList<RegistryObject<B>> blocks = new ArrayList<>(4);
        for (final WeatherState weatherState : wStates) {
            String prefix = weatherState.equals(WeatherState.UNAFFECTED) ? "" : weatherState.name().toLowerCase() + "_";
            Supplier<? extends B> supplier = () -> function.apply(weatherState);
            blocks.add(register(name.apply(prefix), supplier, tab));
        }
        return blocks;
    }

    public static <B extends Block> List<RegistryObject<B>> registerWeatheringSet(String name, Function<WeatherState, B> function, CreativeModeTab tab) {
        return registerWeatheringSet(prefix -> prefix + name, function, tab);
    }

    public static <B extends Block> List<RegistryObject<B>> registerWaxedSet(String name, Function<WeatherState, B> function, CreativeModeTab tab) {
        return registerWeatheringSet(prefix -> "waxed_" + prefix + name, function, tab);
    }

    public static class CopperSet<T extends Block> {

        public static <T extends Block> CopperSet<T> empty() {
            return new CopperSet<>(null, Collections.emptyList());
        }

        private final @Nullable Supplier<T> unaffected;
        private final Collection<Supplier<T>> weathered;

        public CopperSet(@Nullable Supplier<T> unaffected, Collection<Supplier<T>> weathered) {
            this.unaffected = unaffected;
            this.weathered = weathered;
        }

        public Optional<Supplier<T>> unaffected() {
            return Optional.ofNullable(unaffected);
        }

        public Stream<Supplier<T>> weathered() {
            return weathered.stream();
        }

        public Stream<Supplier<T>> all() {
            return Stream.of(Stream.ofNullable(unaffected), weathered()).flatMap(Function.identity());
        }

    }
}
