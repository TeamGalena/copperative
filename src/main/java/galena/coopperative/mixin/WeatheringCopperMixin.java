package galena.coopperative.mixin;

import galena.coopperative.index.CConversions;
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
        return CConversions.getUnweatheredVersion(state.getBlock()).map(previous ->
                previous.withPropertiesOf(state)
        ).or(() ->
                WeatheringCopper.getPrevious(state.getBlock()).map((block -> block.withPropertiesOf(state)))
        );
    }

    /**
     * @author Xaidee Quartz
     * @reason The list of copper states is missing a way to add other blocks.
     */
    @Overwrite
    default Optional<BlockState> getNext(BlockState state) {
        return CConversions.getWeatheredVersion(state.getBlock()).map(next ->
                next.withPropertiesOf(state)
        ).or(() ->
                WeatheringCopper.getNext(state.getBlock()).map((block -> block.withPropertiesOf(state)))
        );
    }

    /**
     * @author Xaidee Quartz
     * @reason The list of copper states is missing a way to add other blocks.
     */
    @Overwrite
    static BlockState getFirst(BlockState state) {
        var first = CConversions.getFirst(state.getBlock());

        if(state.is(first)) {
            return WeatheringCopper.getFirst(first).withPropertiesOf(state);
        } else {
            return first.withPropertiesOf(state);
        }
    }
}
