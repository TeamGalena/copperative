package galena.copperative.content.block.weatheringvanilla;

import galena.copperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.SoundType;

@MethodsReturnNonnullByDefault
public class WeatheringDropperBlock extends DropperBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheringDropperBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.DROPPER).sound(SoundType.COPPER).mapColor(CWeatheringCopper.colorFor(weatherState)));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
