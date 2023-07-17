package galena.copperative.content.block.compat;

import galena.copperative.content.block.CWeatheringCopper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CrankBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.NotNull;

public class WeatheredCrank extends CrankBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheredCrank(WeatherState weatherState) {
        super(Properties.of(Material.WOOD, MaterialColor.NONE).strength(0.6F, 0.6F).noCollission().noOcclusion());
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
