package galena.copperative.content.block;

import galena.copperative.config.CommonConfig;
import galena.copperative.index.CConversions;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Optional;
import java.util.function.Predicate;

public interface CWeatheringCopper extends WeatheringCopper {

    default Optional<BlockState> getNext(BlockState state) {
        return CConversions.getWeatheredVersion(state.getBlock()).map((block) -> block.withPropertiesOf(state));
    }

    default float getChanceModifier() {
        return this.getAge() == WeatherState.UNAFFECTED ? 0.75F : 1.0F;
    }

    default void insert(Block block, boolean before, NonNullList<ItemStack> items, Predicate<ItemStack> filter, boolean startFromExposed) {
        if(CommonConfig.isOverwriteDisabled(block, CommonConfig.OverrideTarget.WEATHERING)) return;

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

    static MaterialColor colorFor(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED ->  MaterialColor.COLOR_ORANGE;
            case EXPOSED ->  MaterialColor.TERRACOTTA_LIGHT_GRAY;
            case WEATHERED ->  MaterialColor.WARPED_STEM;
            case OXIDIZED ->  MaterialColor.WARPED_NYLIUM;
        };
    }
}
