package galena.coopperative.config;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public class CommonConfig {
    private static CommonConfig INSTANCE;

    private final static List<Block> overwrittenBlocks = List.of(
            Blocks.REPEATER,
            Blocks.COMPARATOR,
            Blocks.PISTON,
            Blocks.STICKY_PISTON,
            Blocks.DISPENSER,
            Blocks.DROPPER,
            Blocks.OBSERVER,
            Blocks.LEVER,
            Blocks.POWERED_RAIL
    );

    public static void register() {
        var configured = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        INSTANCE = configured.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configured.getRight());
    }

    public static boolean isPossibleOverwrite(Block block) {
        return overwrittenBlocks.contains(block);
    }

    public static Stream<Block> getPossibleOverwrites() {
        return overwrittenBlocks.stream();
    }

    public static Stream<Block> getOverwrittenBlocks() {
        return getPossibleOverwrites().filter(CommonConfig::isOverwriteEnabled);
    }

    public static boolean isOverwriteEnabled(Block block) {
        return INSTANCE.enabledOverwrites.getOrDefault(block, () -> false).getAsBoolean();
    }

    public static boolean isOverwriteDisabled(Block block) {
        return !INSTANCE.enabledOverwrites.getOrDefault(block, () -> true).getAsBoolean();
    }

    private final Map<Block, BooleanSupplier> enabledOverwrites = new HashMap<>();

    public CommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Enabled Copper Overrides");
        getPossibleOverwrites().forEach(block -> {
            var key = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
            var property = builder.define(key.getPath(), true);
            enabledOverwrites.put(block, property::get);
        });
        builder.pop();
    }

}
