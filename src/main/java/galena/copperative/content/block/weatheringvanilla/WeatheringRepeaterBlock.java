package galena.copperative.content.block.weatheringvanilla;

import galena.copperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
public class WeatheringRepeaterBlock extends RepeaterBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheringRepeaterBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.REPEATER).sound(SoundType.COPPER));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
