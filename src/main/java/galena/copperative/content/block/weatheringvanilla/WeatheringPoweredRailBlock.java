package galena.copperative.content.block.weatheringvanilla;

import galena.copperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.SoundType;

@MethodsReturnNonnullByDefault
public class WeatheringPoweredRailBlock extends PoweredRailBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheringPoweredRailBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.POWERED_RAIL).sound(SoundType.COPPER), true);
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
