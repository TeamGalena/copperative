package galena.copperative.content.block.compat;

import galena.copperative.content.block.CWeatheringCopper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RelayerBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class WeatheredRelayer extends RelayerBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheredRelayer(WeatherState weatherState) {
        super(BlockBehaviour.Properties.copy(Blocks.OBSERVER).randomTicks());
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.is(ModRegistry.RELAYER.get().asItem()), true);
    }

}
