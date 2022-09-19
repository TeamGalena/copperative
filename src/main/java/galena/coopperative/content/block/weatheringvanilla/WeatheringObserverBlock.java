package galena.coopperative.content.block.weatheringvanilla;

import galena.coopperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.Random;

@MethodsReturnNonnullByDefault
public class WeatheringObserverBlock extends ObserverBlock implements CWeatheringCopper {

    private final WeatheringCopper.WeatherState weatherState;

    public WeatheringObserverBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.OBSERVER).sound(SoundType.COPPER));
        this.weatherState = weatherState;
    }

    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        this.onRandomTick(state, world, pos, random);
        super.randomTick(state, world, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).isPresent() || super.isRandomlyTicking;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
