package galena.coopperative.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class CopperDoorBlock extends DoorBlock implements CWeatheringCopper {

    private final WeatherState weatherState;

    public CopperDoorBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    private int getCloseSound() {
        return 1011;
    }

    private int getOpenSound() {
        return 1005;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(POWERED)) return InteractionResult.PASS;
        state = state.cycle(OPEN);
        world.setBlock(pos, state, 10);
        world.levelEvent(player, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        world.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_52779_, BlockPos adjPos, boolean p_52781_) {
        if (!world.isClientSide) {
            var otherState = world.getBlockState(adjPos);
            if (otherState.getBlock() instanceof CopperDoorBlock) this.weatherOtherHalf(otherState, world, adjPos);
        }
        super.neighborChanged(state, world, pos, p_52779_, adjPos, p_52781_);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER && Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).isPresent();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.onRandomTick(state, world, pos, random);
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.getItem().equals(Items.IRON_DOOR), false);
    }

    protected void weatherOtherHalf(BlockState state, Level world, BlockPos pos) {
        BlockPos otherHalfPos = state.getValue(HALF).equals(DoubleBlockHalf.UPPER) ? pos.below() : pos.above();
        BlockState otherHalfState = world.getBlockState(otherHalfPos);

        if (otherHalfState.is(state.getBlock())) return;

        var newState = state.getBlock().withPropertiesOf(otherHalfState);
        world.setBlock(otherHalfPos, newState, 2);
        world.levelEvent(3005, otherHalfPos, 0);
    }
}
