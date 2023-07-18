package galena.copperative.content.block.weatheringvanilla;

import galena.copperative.content.block.CWeatheringCopper;
import galena.copperative.index.CConversions;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

@MethodsReturnNonnullByDefault
public class WeatheringLeverBlock extends LeverBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheringLeverBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.LEVER).sound(SoundType.COPPER));
        this.weatherState = weatherState;
    }

    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.onRandomTick(state, world, pos, random);
        super.randomTick(state, world, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return CConversions.getWeatheredVersion(state.getBlock()).isPresent() || super.isRandomlyTicking(state);
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
