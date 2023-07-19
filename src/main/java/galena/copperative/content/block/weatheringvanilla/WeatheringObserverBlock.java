package galena.copperative.content.block.weatheringvanilla;

import galena.copperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class WeatheringObserverBlock extends ObserverBlock implements CWeatheringCopper {

    private final WeatheringCopper.WeatherState weatherState;

    public WeatheringObserverBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.OBSERVER).sound(SoundType.COPPER).mapColor(CWeatheringCopper.colorFor(weatherState)));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        var facing = state.getValue(FACING);
        return direction == facing;
    }
}
