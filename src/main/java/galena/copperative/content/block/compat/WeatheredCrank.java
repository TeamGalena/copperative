package galena.copperative.content.block.compat;

import galena.copperative.content.block.CWeatheringCopper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CrankBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class WeatheredCrank extends CrankBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheredCrank(WeatherState weatherState) {
        super(Properties.of().mapColor(MapColor.NONE).pushReaction(PushReaction.DESTROY).strength(0.6F, 0.6F).noCollission().noOcclusion());
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

}
