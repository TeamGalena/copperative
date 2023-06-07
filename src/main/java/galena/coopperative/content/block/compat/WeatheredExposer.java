package galena.coopperative.content.block.compat;

import galena.coopperative.content.block.CWeatheringCopper;
import galena.oreganized.content.block.ExposerBlock;
import galena.oreganized.index.OBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;

public class WeatheredExposer extends ExposerBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheredExposer(WeatherState weatherState) {
        super(Properties.copy(Blocks.OBSERVER).randomTicks());
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, IPlantable plantable) {
        return false;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.is(OBlocks.EXPOSER.get().asItem()), true);
    }
}
