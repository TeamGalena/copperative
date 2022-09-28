package galena.coopperative.content.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import galena.coopperative.Coopperative;
import galena.coopperative.index.CBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface CWeatheringCopper extends WeatheringCopper {

    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .putAll(WeatheringCopper.NEXT_BY_BLOCK.get())
            .putAll(Coopperative.WEATHERING_BLOCKS.get())
            .build());
    Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

    static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(block));
    }

    static Block getFirst(Block block) {
        Block newBlock = block;

        for(Block firstBlock = PREVIOUS_BY_BLOCK.get().get(block); firstBlock != null; firstBlock = PREVIOUS_BY_BLOCK.get().get(firstBlock)) {
            newBlock = firstBlock;
        }

        return newBlock;
    }

    static Optional<BlockState> getPrevious(BlockState state) {
        return getPrevious(state.getBlock()).map((block) -> block.withPropertiesOf(state));
    }

    static Optional<Block> getNext(Block block) {
        return Optional.ofNullable(Coopperative.WEATHERING_BLOCKS.get().get(block));
    }

    static BlockState getFirst(BlockState state) {
        return getFirst(state.getBlock()).withPropertiesOf(state);
    }

    default Optional<BlockState> getNext(BlockState state) {
        return getNext(state.getBlock()).map((block) -> block.withPropertiesOf(state));
    }

    default float getChanceModifier() {
        return this.getAge() == WeatherState.UNAFFECTED ? 0.75F : 1.0F;
    }

    default void insert(Block block, boolean before, NonNullList<ItemStack> items, Predicate<ItemStack> filter, boolean startFromExposed) {
        ItemStack stack = new ItemStack(block);
        WeatherState weatherState = ((CWeatheringCopper) block).getAge();
        int offset = (weatherState.ordinal() - (startFromExposed ? 1 : 0)) * (before ? -1 : 1);
        if (items.stream().anyMatch(filter)) {
            Optional<ItemStack> optional = items.stream().filter(filter).max((a, b) ->
            {
                int valA = items.indexOf(a);
                int valB = items.indexOf(b);
                if (valA == -1 && valB == -1)
                    return 0;
                if (valA == -1)
                    return valB;
                if (valB == -1)
                    return valA;
                return before ? valB - valA : valA - valB;
            });
            if (optional.isPresent()) {
                items.add(items.indexOf(optional.get()) + (before ? 0 : 1) + offset, stack);
                return;
            }
        }
        items.add(stack);
    }
}
