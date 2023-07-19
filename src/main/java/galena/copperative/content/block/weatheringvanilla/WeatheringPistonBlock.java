package galena.copperative.content.block.weatheringvanilla;

import galena.copperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.piston.PistonBaseBlock;

@MethodsReturnNonnullByDefault
public class WeatheringPistonBlock extends PistonBaseBlock implements CWeatheringCopper {

    private final WeatherState weatherState;
    private final boolean isSticky;

    public WeatheringPistonBlock(WeatherState weatherState, boolean isSticky) {
        super(isSticky, Properties.copy(isSticky ? Blocks.STICKY_PISTON : Blocks.PISTON).sound(SoundType.COPPER).mapColor(CWeatheringCopper.colorFor(weatherState)));
        this.weatherState = weatherState;
        this.isSticky = isSticky;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
