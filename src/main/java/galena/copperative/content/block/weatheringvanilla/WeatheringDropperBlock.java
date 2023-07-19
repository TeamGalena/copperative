package galena.copperative.content.block.weatheringvanilla;

import galena.copperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
public class WeatheringDropperBlock extends DropperBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheringDropperBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.DROPPER).sound(SoundType.COPPER).color(CWeatheringCopper.colorFor(weatherState)));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.getItem().equals(Items.DROPPER), true);
    }
}
