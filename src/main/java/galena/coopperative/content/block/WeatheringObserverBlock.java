package galena.coopperative.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class WeatheringObserverBlock extends ObserverBlock implements WeatheringCopper {

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
        return WeatheringCopper.getNext(state.getBlock()).isPresent() || super.isRandomlyTicking;
    }

    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }
}
