package galena.coopperative.index;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import galena.coopperative.config.CommonConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CConversions {

    private static final Supplier<BiMap<Block, Block>> WEATHERING_BLOCKS = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
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

            .putAll(blockMapFromArray(CBlocks.COPPER_DOORS))
            .putAll(blockMapFromArray(CBlocks.COPPER_TRAPDOORS))

            .putAll(blockMapFromArray(CBlocks.EXPOSERS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.RELAYERS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.CRANKS.all().toList()))
            .build());
    private static final Supplier<BiMap<Block, Block>> WAXED_BLOCKS = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .putAll(waxedEntries(CBlocks.COPPER_BRICKS, CBlocks.WAXED_COPPER_BRICKS))
            .putAll(waxedEntries(CBlocks.COPPER_TILES, CBlocks.WAXED_COPPER_TILES))
            .putAll(waxedEntries(CBlocks.COPPER_PILLAR, CBlocks.WAXED_COPPER_PILLAR))
            .putAll(waxedEntries(CBlocks.COPPER_DOORS, CBlocks.WAXED_COPPER_DOORS))
            .putAll(waxedEntries(CBlocks.COPPER_TRAPDOORS, CBlocks.WAXED_COPPER_TRAPDOORS))
            .build());

    public static Optional<Block> getWaxedVersion(Block block) {
        return Optional.ofNullable(WAXED_BLOCKS.get().get(block));
    }

    public static Optional<Block> getUnwaxedVersion(Block block) {
        return Optional.ofNullable(WAXED_BLOCKS.get().inverse().get(block));
    }

    public static Optional<Block> getWeatheredVersion(Block block) {
        if (CommonConfig.isOverwriteDisabled(block, CommonConfig.OverrideTarget.WEATHERING)) return Optional.empty();
        return Optional.ofNullable(WEATHERING_BLOCKS.get().get(block));
    }

    public static Optional<Block> getUnweatheredVersion(Block block) {
        return Optional.ofNullable(WEATHERING_BLOCKS.get().inverse().get(block));
    }

    public static Stream<Map.Entry<Block, Block>> getWaxedPairs() {
        return WAXED_BLOCKS.get().entrySet().stream();
    }

    public static Stream<Map.Entry<Block, Block>> getWeatheredPairs() {
        return WEATHERING_BLOCKS.get().entrySet().stream();
    }

    public static Block getFirst(Block block) {
        var first = block;

        while (true) {
            var previous = CConversions.getUnweatheredVersion(first);
            if (previous.isEmpty()) break;
            first = previous.get();
        }

        return first;
    }

    private static <T extends Block, R extends Block> ImmutableBiMap<Block, Block> waxedEntries(List<? extends Supplier<T>> unwaxedList, List<? extends Supplier<R>> waxedList) {
        if (unwaxedList.size() != waxedList.size())
            throw new IllegalArgumentException("waxed and unwaxed lists are not equals in size");

        ImmutableBiMap.Builder<Block, Block> map = new ImmutableBiMap.Builder<>();

        for (int i = 0; i < waxedList.size(); i++) {
            map.put(unwaxedList.get(i).get(), waxedList.get(i).get());
        }

        return map.build();
    }

    private static <B extends Block> ImmutableBiMap<Block, Block> blockMapFromArray(List<? extends Supplier<B>> blockArrayList) {
        ImmutableBiMap.Builder<Block, Block> map = new ImmutableBiMap.Builder<>();

        if (!blockArrayList.isEmpty())
            for (int i = 0; blockArrayList.size() - 1 > i; i++)
                map.put(blockArrayList.get(i).get(), blockArrayList.get(i + 1).get());

        return map.build();
    }

}
