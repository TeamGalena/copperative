package galena.coopperative.mixin;

import galena.coopperative.Coopperative;
import galena.coopperative.index.CBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Optional;

@Mixin(WeatheringCopper.class)
public interface WeatheringCopperMixin {


    /**
     * @author Xaidee Quartz
     * @reason The list of copper states is missing a way to add other blocks.
     */
    @Overwrite
    static Optional<BlockState> getPrevious(BlockState state) {
        if (Coopperative.WEATHERING_BLOCKS.get().inverse().get(state.getBlock()) != null)
            return Optional.of(Coopperative.WEATHERING_BLOCKS.get().inverse().get(state.getBlock()).withPropertiesOf(state));
        return WeatheringCopper.getPrevious(state.getBlock()).map((block) -> block.withPropertiesOf(state));
    }

    /**
     * @author Xaidee Quartz
     * @reason The list of copper states is missing a way to add other blocks.
     */
    @Overwrite
    default Optional<BlockState> getNext(BlockState state) {
        if (Coopperative.WEATHERING_BLOCKS.get().get(state.getBlock()) != null)
            return Optional.of(Coopperative.WEATHERING_BLOCKS.get().get(state.getBlock()).withPropertiesOf(state));

        return WeatheringCopper.getNext(state.getBlock()).map((block -> block.withPropertiesOf(state)));
    }

    /**
     * @author Xaidee Quartz
     * @reason The list of copper states is missing a way to add other blocks.
     */
    @Overwrite
    static BlockState getFirst(BlockState state) {
        if (Coopperative.WEATHERING_BLOCKS.get().inverse().get(state.getBlock()) != null) {
            Block block = state.getBlock();

            for(Block block1 = Coopperative.WEATHERING_BLOCKS.get().get(state.getBlock()); block1 != null; block1 = Coopperative.WEATHERING_BLOCKS.get().get(block1)) {
                block = block1;
            }

            return block.withPropertiesOf(state);
        }
        return WeatheringCopper.getFirst(state.getBlock().withPropertiesOf(state));
    }
}
