package galena.copperative.content.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

public class CopperTrapDoorBlock extends AbstractCopperTrapdoorBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public CopperTrapDoorBlock(WeatherState weatherState, Properties properties, BlockSetType blockSetType) {
        super(properties, blockSetType);
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }
}
