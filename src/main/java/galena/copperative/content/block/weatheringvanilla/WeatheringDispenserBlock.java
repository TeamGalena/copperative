package galena.copperative.content.block.weatheringvanilla;

import galena.copperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SoundType;

@MethodsReturnNonnullByDefault
public class WeatheringDispenserBlock extends DispenserBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheringDispenserBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.DISPENSER).sound(SoundType.COPPER).mapColor(CWeatheringCopper.colorFor(weatherState)));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
