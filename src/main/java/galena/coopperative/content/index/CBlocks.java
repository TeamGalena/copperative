package galena.coopperative.content.index;

import galena.coopperative.Coopperative;
import galena.coopperative.content.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class CBlocks {

    public static final CreativeModeTab REDSTONE = CreativeModeTab.TAB_REDSTONE;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Coopperative.MOD_ID);

    //public static final RegistryObject<Block> SPOT_LIGHT = register("spot_light", () -> new SpotLightBlock(BlockBehaviour.Properties.copy(Blocks.AIR).lightLevel(SpotLightBlock.LIGHT_EMISSION)));

    public static final RegistryObject<Block> PATINA_BLOCK = register("patina_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).sound(SoundType.TUFF)), CreativeModeTab.TAB_BUILDING_BLOCKS);

    // Redstone Components
    public static final RegistryObject<Block> EXPOSED_OBSERVER = register("exposed_observer", () -> new WeatheringObserverBlock(WeatheringCopper.WeatherState.EXPOSED), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> WEATHERED_OBSERVER = register("weathered_observer", () -> new WeatheringObserverBlock(WeatheringCopper.WeatherState.WEATHERED), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_OBSERVER = register("oxidized_observer", () -> new WeatheringObserverBlock(WeatheringCopper.WeatherState.OXIDIZED), CreativeModeTab.TAB_REDSTONE);


    public static final RegistryObject<DoorBlock> COPPER_DOOR = register("copper_door", () -> new CopperDoorBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<DoorBlock> EXPOSED_COPPER_DOOR = register("exposed_copper_door", () -> new CopperDoorBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<DoorBlock> WEATHERED_COPPER_DOOR = register("weathered_copper_door", () -> new CopperDoorBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<DoorBlock> OXIDIZED_COPPER_DOOR = register("oxidized_copper_door", () -> new CopperDoorBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), CreativeModeTab.TAB_REDSTONE);

    public static final RegistryObject<TrapDoorBlock> COPPER_TRAPDOOR = register("copper_trapdoor", () -> new CopperTrapDoorBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).sound(SoundType.COPPER)), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<TrapDoorBlock> EXPOSED_COPPER_TRAPDOOR = register("exposed_copper_trapdoor", () -> new CopperTrapDoorBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<TrapDoorBlock> WEATHERED_COPPER_TRAPDOOR = register("weathered_copper_trapdoor", () -> new CopperTrapDoorBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<TrapDoorBlock> OXIDIZED_COPPER_TRAPDOOR = register("oxidized_copper_trapdoor", () -> new CopperTrapDoorBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(COPPER_DOOR.get())), CreativeModeTab.TAB_REDSTONE);

    //public static final RegistryObject<Block> HEADLIGHT = register("headlight", () -> new HeadLightBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)), CreativeModeTab.TAB_REDSTONE);

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> block, CreativeModeTab tab) {
        RegistryObject<B> blocks = BLOCKS.register(name, block);
        CItems.ITEMS.register(name, () -> new BlockItem(blocks.get(), new Item.Properties().tab(tab)));
        return blocks;
    }

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> block) {
        return BLOCKS.register(name, block);
    }

    // For Blocks that only register when other mods are loaded (Compatibility)
    public static <B extends Block> RegistryObject<B> register(String modid, String name, Supplier<? extends B> block, CreativeModeTab tab) {
        if (ModList.get().isLoaded(modid)) {
            return register(name, block, tab);
        }
        return null;
    }
}
