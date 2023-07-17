package galena.copperative.content.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class CopperTrapDoorBlock extends AbstractCopperTrapdoorBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public CopperTrapDoorBlock(WeatherState weatherState, Properties properties, boolean canBeUsedByPlayers) {
        super(properties, canBeUsedByPlayers);
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.getItem().equals(Items.IRON_TRAPDOOR), false);
    }
}
