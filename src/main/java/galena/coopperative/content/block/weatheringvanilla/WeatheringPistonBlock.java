package galena.coopperative.content.block.weatheringvanilla;

import galena.coopperative.content.block.CWeatheringCopper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
public class WeatheringPistonBlock extends PistonBaseBlock implements CWeatheringCopper {

    private final WeatherState weatherState;
    private final boolean isSticky;

    public WeatheringPistonBlock(WeatherState weatherState, boolean isSticky) {
        super(isSticky, Properties.copy(isSticky ? Blocks.STICKY_PISTON : Blocks.PISTON).sound(SoundType.COPPER));
        this.weatherState = weatherState;
        this.isSticky = isSticky;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.getItem().equals(this.isSticky ? Items.STICKY_PISTON : Items.PISTON), true);
    }
}
