package galena.copperative.content.block.compat;

import galena.copperative.content.block.CWeatheringCopper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RelayerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WeatheredRelayer extends RelayerBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheredRelayer(WeatherState weatherState) {
        super(BlockBehaviour.Properties.copy(Blocks.OBSERVER).randomTicks().mapColor(CWeatheringCopper.colorFor(weatherState)));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

}
