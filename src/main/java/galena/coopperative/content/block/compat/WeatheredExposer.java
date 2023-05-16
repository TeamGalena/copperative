package galena.coopperative.content.block.compat;

import galena.coopperative.content.block.CWeatheringCopper;
import galena.oreganized.content.block.ExposerBlock;
import galena.oreganized.index.OBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;

public class WeatheredExposer extends ExposerBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheredExposer(WeatherState weatherState) {
        super(Properties.copy(OBlocks.EXPOSER.get()));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, IPlantable plantable) {
        return false;
    }
}
