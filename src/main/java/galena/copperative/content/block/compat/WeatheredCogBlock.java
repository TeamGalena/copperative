package galena.copperative.content.block.compat;

import galena.copperative.content.block.CWeatheringCopper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CogBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

public class WeatheredCogBlock extends CogBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheredCogBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.COPPER_BLOCK).strength(3.0F, 6.0F).sound(SoundType.COPPER).requiresCorrectToolForDrops().color(CWeatheringCopper.colorFor(weatherState)));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.is(ModRegistry.COG_BLOCK.get().asItem()), true);
    }
}
