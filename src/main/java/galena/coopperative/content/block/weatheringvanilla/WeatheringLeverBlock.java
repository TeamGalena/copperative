package galena.coopperative.content.block.weatheringvanilla;

import galena.coopperative.content.block.CWeatheringCopper;
import galena.coopperative.index.CConversions;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
public class WeatheringLeverBlock extends LeverBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public WeatheringLeverBlock(WeatherState weatherState) {
        super(Properties.copy(Blocks.LEVER).sound(SoundType.COPPER));
        this.weatherState = weatherState;
    }

    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.onRandomTick(state, world, pos, random);
        super.randomTick(state, world, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return CConversions.getWeatheredVersion(state.getBlock()).isPresent() || super.isRandomlyTicking(state);
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.getItem().equals(Items.LEVER), true);
    }
}
