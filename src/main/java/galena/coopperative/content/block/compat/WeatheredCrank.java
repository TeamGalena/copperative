package galena.coopperative.content.block.compat;

import galena.coopperative.content.block.CWeatheringCopper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CrankBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class WeatheredCrank extends CrankBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheredCrank(WeatherState weatherState) {
        super(Properties.copy(Blocks.OBSERVER).randomTicks());
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.is(ModRegistry.CRANK.get().asItem()), true);
    }

}
