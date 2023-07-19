package galena.copperative.content.block.weatheringvanilla;

import galena.copperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
public class WeatheringDispenserBlock extends DispenserBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheringDispenserBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.DISPENSER).sound(SoundType.COPPER).color(CWeatheringCopper.colorFor(weatherState)));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.getItem().equals(Items.DISPENSER), true);
    }
}
