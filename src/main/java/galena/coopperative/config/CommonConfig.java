package galena.coopperative.config;

import com.google.common.collect.ImmutableMap;
import galena.coopperative.Coopperative;
import galena.coopperative.index.CConversions;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CommonConfig {

    public enum OverrideTarget {
        APPEARANCE, RECIPE;
    }

    private record OverrideEntry(BooleanSupplier enabled, Map<OverrideTarget, BooleanSupplier> targets) {
        boolean isEnabled(OverrideTarget target) {
            return enabled.getAsBoolean() && targets.get(target).getAsBoolean();
        }
    }

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
        var builder = ConfigBuilder.create(Coopperative.MOD_ID, ConfigType.COMMON);
        builder.setSynced();
        INSTANCE = new CommonConfig(builder);
        builder.buildAndRegister().loadFromFile();
    }

    public static boolean isPossibleOverwrite(Block block) {
        var first = CConversions.getFirst(block);
        return overwrittenBlocks.contains(first);
    }

    public static Collection<Block> getPossibleOverwrites() {
        return overwrittenBlocks;
    }

    public static Stream<Block> getOverwrittenBlocks(OverrideTarget target) {
        return getPossibleOverwrites().stream().filter(it -> isOverwriteEnabled(it, target));
    }

    private static boolean test(Block block, Predicate<OverrideEntry> func) {
        var first = CConversions.getFirst(block);
        if (!INSTANCE.enabledOverwrites.containsKey(first)) return false;
        return func.test(INSTANCE.enabledOverwrites.get(first));
    }

    public static boolean isOverwriteEnabled(Block block, OverrideTarget target) {
        return test(block, it -> it.isEnabled(target));
    }

    public static boolean isOverwriteDisabled(Block block, OverrideTarget target) {
        return test(block, it -> !it.isEnabled(target));
    }

    public static boolean isOverwriteEnabled(Block block) {
        return test(block, it -> it.enabled.getAsBoolean());
    }

    public static boolean isOverwriteDisabled(Block block) {
        return test(block, it -> !it.enabled.getAsBoolean());
    }

    private final Map<Block, OverrideEntry> enabledOverwrites = new HashMap<>();

    public CommonConfig(ConfigBuilder builder) {
        builder.push("Enabled Copper Overrides");
        getPossibleOverwrites().forEach(block -> {
            var key = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
            builder.push(key.getPath());

            var property = builder.define("enabled", true);
            var targets = ImmutableMap.<OverrideTarget, BooleanSupplier>builder();
            for (OverrideTarget target : OverrideTarget.values()) {
                var targetProperty = builder.define(target.name().toLowerCase(), true);
                targets.put(target, targetProperty::get);
            }

            enabledOverwrites.put(block, new OverrideEntry(property::get, targets.build()));

            builder.pop();
        });
        builder.pop();
    }

}
