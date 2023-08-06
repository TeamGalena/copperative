package galena.copperative.index;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import galena.copperative.config.CommonConfig;
import net.minecraft.world.level.block.Block;

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

            .putAll(blockMapFromArray(CBlocks.REPEATERS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.COMPARATORS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.PISTONS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.STICKY_PISTONS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.OBSERVERS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.DISPENSERS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.DROPPERS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.LEVERS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.POWERED_RAILS.all().toList()))

            .putAll(blockMapFromArray(CBlocks.COPPER_DOORS))
            .putAll(blockMapFromArray(CBlocks.COPPER_TRAPDOORS))

            .putAll(blockMapFromArray(CBlocks.EXPOSERS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.RELAYERS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.CRANKS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.COG_BLOCKS.all().toList()))
            .putAll(blockMapFromArray(CBlocks.RANDOMIZERS.all().toList()))
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
