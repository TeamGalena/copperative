package galena.copperative.index;

import galena.copperative.Copperative;
import galena.copperative.content.block.AbstractCopperDoorBlock;
import galena.copperative.content.block.AbstractCopperTrapdoorBlock;
import galena.copperative.content.block.CWeatheringCopper;
import galena.copperative.content.block.CopperDoorBlock;
import galena.copperative.content.block.CopperTrapDoorBlock;
import galena.copperative.content.block.HeadLightBlock;
import galena.copperative.content.block.SpotLightBlock;
import galena.copperative.content.block.TogglerBlock;
import galena.copperative.content.block.WeatheringPillarBlock;
import galena.copperative.content.block.compat.WeatheredCogBlock;
import galena.copperative.content.block.compat.WeatheredCrank;
import galena.copperative.content.block.compat.WeatheredRelayer;
import galena.copperative.content.block.tile.HeadlightTile;
import galena.copperative.content.block.weatheringvanilla.WeatheringComparatorBlock;
import galena.copperative.content.block.weatheringvanilla.WeatheringDispenserBlock;
import galena.copperative.content.block.weatheringvanilla.WeatheringDropperBlock;
import galena.copperative.content.block.weatheringvanilla.WeatheringLeverBlock;
import galena.copperative.content.block.weatheringvanilla.WeatheringObserverBlock;
import galena.copperative.content.block.weatheringvanilla.WeatheringPistonBlock;
import galena.copperative.content.block.weatheringvanilla.WeatheringPoweredRailBlock;
import galena.copperative.content.block.weatheringvanilla.WeatheringRepeaterBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.WeatheringCopperFullBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static galena.copperative.content.block.CWeatheringCopper.colorFor;
import static net.minecraft.world.item.CreativeModeTabs.NATURAL_BLOCKS;

public class CBlocks {

    public static final ResourceKey<CreativeModeTab> REDSTONE = CreativeModeTabs.REDSTONE_BLOCKS;
    public static final ResourceKey<CreativeModeTab> TRANSPORT = CreativeModeTabs.REDSTONE_BLOCKS;
    public static final ResourceKey<CreativeModeTab> BUILDING = CreativeModeTabs.BUILDING_BLOCKS;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Copperative.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Copperative.MOD_ID);

    public static final RegistryObject<SpotLightBlock> SPOT_LIGHT = BLOCKS.register("spot_light", () -> new SpotLightBlock(Properties.of().noCollission().noLootTable().noOcclusion().lightLevel(SpotLightBlock.LIGHT_EMISSION)));

    // Storage Blocks
    public static final RegistryObject<Block> PATINA_BLOCK = register("patina_block", () -> new SandBlock(0xBD3A0, Properties.copy(Blocks.SAND).sound(SoundType.TUFF)));

    static {
        CTabs.addToTab(PATINA_BLOCK, () -> Blocks.RAW_GOLD_BLOCK, NATURAL_BLOCKS);
    }

    // Decorative Blocks
    public static final List<RegistryObject<WeatheringCopperFullBlock>> COPPER_BRICKS = registerWeatheringSet("copper_bricks", weatherState -> new WeatheringCopperFullBlock(weatherState, Properties.copy(Blocks.CUT_COPPER).mapColor(colorFor(weatherState))), BUILDING);
    public static final List<RegistryObject<WeatheringPillarBlock>> COPPER_PILLAR = registerWeatheringSet("copper_pillar", weatherState -> new WeatheringPillarBlock(weatherState, Properties.copy(Blocks.CUT_COPPER).mapColor(colorFor(weatherState))), BUILDING);
    public static final List<RegistryObject<WeatheringPillarBlock>> COPPER_TILES = registerWeatheringSet("copper_tiles", weatherState -> new WeatheringPillarBlock(weatherState, Properties.copy(Blocks.CUT_COPPER).mapColor(colorFor(weatherState))), BUILDING);

    public static final List<RegistryObject<Block>> WAXED_COPPER_BRICKS = registerWaxedSet("copper_bricks", weatherState -> new Block(Properties.copy(Blocks.CUT_COPPER).mapColor(colorFor(weatherState))), BUILDING);
    public static final List<RegistryObject<RotatedPillarBlock>> WAXED_COPPER_PILLAR = registerWaxedSet("copper_pillar", weatherState -> new RotatedPillarBlock(Properties.copy(Blocks.CUT_COPPER).mapColor(colorFor(weatherState))), BUILDING);
    public static final List<RegistryObject<RotatedPillarBlock>> WAXED_COPPER_TILES = registerWaxedSet("copper_tiles", weatherState -> new RotatedPillarBlock(Properties.copy(Blocks.CUT_COPPER).mapColor(colorFor(weatherState))), BUILDING);

    // Redstone Components
    public static final CopperSet<Block> REPEATERS = registerConvertedSet("repeater", () -> Blocks.REPEATER, WeatheringRepeaterBlock::new, REDSTONE);

    public static final CopperSet<Block> COMPARATORS = registerConvertedSet("comparator", () -> Blocks.COMPARATOR, WeatheringComparatorBlock::new, REDSTONE);

    public static final CopperSet<Block> PISTONS = registerConvertedSet("piston", () -> Blocks.PISTON, it -> new WeatheringPistonBlock(it, false), REDSTONE);

    public static final CopperSet<Block> STICKY_PISTONS = registerConvertedSet("sticky_piston", () -> Blocks.STICKY_PISTON, it -> new WeatheringPistonBlock(it, true), REDSTONE);

    public static final CopperSet<Block> OBSERVERS = registerConvertedSet("observer", () -> Blocks.OBSERVER, WeatheringObserverBlock::new, REDSTONE);

    public static final CopperSet<Block> DISPENSERS = registerConvertedSet("dispenser", () -> Blocks.DISPENSER, WeatheringDispenserBlock::new, REDSTONE);

    public static final CopperSet<Block> DROPPERS = registerConvertedSet("dropper", () -> Blocks.DROPPER, WeatheringDropperBlock::new, REDSTONE);

    public static final CopperSet<Block> LEVERS = registerConvertedSet("lever", () -> Blocks.LEVER, WeatheringLeverBlock::new, REDSTONE);

    public static final BlockSetType COPPER_BLOCK_SET = BlockSetType.register(new BlockSetType("copper", true, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
    public static final BlockSetType WEATHERED_COPPER_BLOCK_SET = BlockSetType.register(new BlockSetType("copper", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    public static final List<RegistryObject<DoorBlock>> COPPER_DOORS = registerWeatheringSet("copper_door", it -> new CopperDoorBlock(it, Properties.copy(Blocks.IRON_DOOR).sound(SoundType.COPPER).mapColor(CWeatheringCopper.colorFor(it)), AbstractCopperDoorBlock.blockSetFor(it)), REDSTONE);

    public static final List<RegistryObject<TrapDoorBlock>> COPPER_TRAPDOORS = registerWeatheringSet("copper_trapdoor", it -> new CopperTrapDoorBlock(it, Properties.copy(Blocks.IRON_TRAPDOOR).sound(SoundType.COPPER).mapColor(CWeatheringCopper.colorFor(it)), AbstractCopperDoorBlock.blockSetFor(it)), REDSTONE);

    public static final List<RegistryObject<DoorBlock>> WAXED_COPPER_DOORS = registerWaxedSet("copper_door", it -> new AbstractCopperDoorBlock(Properties.copy(Blocks.IRON_DOOR).sound(SoundType.COPPER).mapColor(CWeatheringCopper.colorFor(it)), AbstractCopperDoorBlock.blockSetFor(it)), REDSTONE);

    public static final List<RegistryObject<TrapDoorBlock>> WAXED_COPPER_TRAPDOORS = registerWaxedSet("copper_trapdoor", it -> new AbstractCopperTrapdoorBlock(Properties.copy(Blocks.IRON_TRAPDOOR).sound(SoundType.COPPER).mapColor(CWeatheringCopper.colorFor(it)), AbstractCopperDoorBlock.blockSetFor(it)), REDSTONE);


    public static final List<RegistryObject<Block>> HEADLIGHT = registerWeatheringSet("headlight", weatherState -> new HeadLightBlock(weatherState, Properties.copy(Blocks.COPPER_BLOCK).lightLevel(HeadLightBlock.LIGHT_EMISSION).mapColor(CWeatheringCopper.colorFor(weatherState))), REDSTONE);
    public static final RegistryObject<BlockEntityType<HeadlightTile>> HEADLIGHT_TILE = BLOCK_ENTITIES.register("headlight", () -> {
        var blocks = HEADLIGHT.stream().map(RegistryObject::get).toArray(Block[]::new);
        return BlockEntityType.Builder.of(HeadlightTile::new, blocks).build(null);
    });

    public static final List<RegistryObject<Block>> TOGGLER = registerWeatheringSet("toggler", weatherState -> new TogglerBlock(weatherState, Properties.copy(Blocks.REPEATER).sound(SoundType.COPPER)), REDSTONE);

    // Rails
    public static final CopperSet<Block> POWERED_RAILS = registerConvertedSet("powered_rail", () -> Blocks.POWERED_RAIL, WeatheringPoweredRailBlock::new, TRANSPORT);

    // Compat
    //public static final CopperSet<Block> EXPOSERS = ifLoaded("oreganized",
    //        () -> registerConvertedSet("exposer", OBlocks.EXPOSER, WeatheredExposer::new, CreativeModeTab.TAB_REDSTONE), CopperSet::empty
    //);
    public static final CopperSet<Block> EXPOSERS = CopperSet.empty();

    public static final CopperSet<Block> RELAYERS = ifLoaded("supplementaries",
            () -> registerConvertedSet("relayer", ModRegistry.RELAYER, WeatheredRelayer::new, REDSTONE), CopperSet::empty
    );

    public static final CopperSet<Block> CRANKS = ifLoaded("supplementaries",
            () -> registerConvertedSet("crank", ModRegistry.CRANK, WeatheredCrank::new, REDSTONE), CopperSet::empty
    );

    public static final CopperSet<Block> COG_BLOCKS = ifLoaded("supplementaries",
            () -> registerConvertedSet("cog_block", ModRegistry.COG_BLOCK, WeatheredCogBlock::new, REDSTONE), CopperSet::empty
    );

    //public static final CopperSet<Block> RANDOMIZERS = ifLoaded("quark",
    //        () -> registerConvertedSet("randomizer", WeatheredRandomizer::loadUnaffected, WeatheredRandomizer::new, REDSTONE), CopperSet::empty
    //);
    public static final CopperSet<Block> RANDOMIZERS = CopperSet.empty();

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> block) {
        RegistryObject<B> supplier = BLOCKS.register(name, block);
        CItems.ITEMS.register(name, () -> new BlockItem(supplier.get(), new Item.Properties()));
        return supplier;
    }

    public static <B extends Block> RegistryObject<B> registerAndAddTo(String name, Supplier<? extends B> block, ResourceKey<CreativeModeTab> tab) {
        RegistryObject<B> supplier = register(name, block);
        CTabs.addToTab(supplier, null, tab);
        return supplier;
    }

    public static <B extends Block> CopperSet<B> registerConvertedSet(String name, Supplier<B> targetSupplier, Function<WeatherState, B> function, ResourceKey<CreativeModeTab> tab) {
        var weathered = Stream.of(WeatherState.EXPOSED, WeatherState.WEATHERED, WeatherState.OXIDIZED).<Supplier<B>>map(weatherState -> {
            String prefix = weatherState.name().toLowerCase(Locale.ROOT) + "_";
            return register(prefix + name, () -> function.apply(weatherState));
        }).toList();
        return new CopperSet<>(targetSupplier, weathered).addToTab(tab);
    }

    public static <R> R ifLoaded(String mod, Supplier<R> supplier, Supplier<R> emptySupplier) {
        if (ModList.get().isLoaded(mod)) return supplier.get();
        else return emptySupplier.get();
    }

    public static <B extends Block> List<RegistryObject<B>> registerWeatheringSet(UnaryOperator<String> name, Function<WeatherState, B> function, ResourceKey<CreativeModeTab> tab) {
        WeatherState[] wStates = WeatherState.values();
        ArrayList<RegistryObject<B>> blocks = new ArrayList<>(4);
        for (final WeatherState weatherState : wStates) {
            String prefix = weatherState.equals(WeatherState.UNAFFECTED) ? "" : weatherState.name().toLowerCase(Locale.ROOT) + "_";
            Supplier<? extends B> supplier = () -> function.apply(weatherState);
            blocks.add(registerAndAddTo(name.apply(prefix), supplier, tab));
        }
        return blocks;
    }

    public static <B extends Block> List<RegistryObject<B>> registerWeatheringSet(String name, Function<WeatherState, B> function, ResourceKey<CreativeModeTab> tab) {
        return registerWeatheringSet(prefix -> prefix + name, function, tab);
    }

    public static <B extends Block> List<RegistryObject<B>> registerWaxedSet(String name, Function<WeatherState, B> function, ResourceKey<CreativeModeTab> tab) {
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

        public CopperSet<T> addToTab(ResourceKey<CreativeModeTab> tab) {
            CTabs.addToTab(weathered, unaffected, tab);
            return this;
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
