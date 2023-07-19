package galena.copperative.content.block.compat;

import galena.copperative.content.block.CWeatheringCopper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CogBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

public class WeatheredCogBlock extends CogBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheredCogBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.COPPER_BLOCK).strength(3.0F, 6.0F).sound(SoundType.COPPER).requiresCorrectToolForDrops().mapColor(CWeatheringCopper.colorFor(weatherState)));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }
}
