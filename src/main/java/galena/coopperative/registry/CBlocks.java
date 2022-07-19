package galena.coopperative.registry;

import galena.coopperative.Coopperative;
import galena.coopperative.block.WeatheringCopperObserverBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CBlocks {

    public static final CreativeModeTab REDSTONE = CreativeModeTab.TAB_REDSTONE;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Coopperative.MOD_ID);

    public static final RegistryObject<Block> OBSERVER = register("observer", () -> new WeatheringCopperObserverBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.OBSERVER).sound(SoundType.COPPER)), REDSTONE);
    public static final RegistryObject<Block> EXPOSED_OBSERVER = register("exposed_observer", () -> new WeatheringCopperObserverBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.copy(Blocks.OBSERVER).sound(SoundType.COPPER)), REDSTONE);
    public static final RegistryObject<Block> WEATHERED_OBSERVER = register("weathered_observer", () -> new WeatheringCopperObserverBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.copy(Blocks.OBSERVER).sound(SoundType.COPPER)), REDSTONE);
    public static final RegistryObject<Block> OXIDIZED_OBSERVER = register("oxidized_observer", () -> new WeatheringCopperObserverBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.copy(Blocks.OBSERVER).sound(SoundType.COPPER)), REDSTONE);

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
