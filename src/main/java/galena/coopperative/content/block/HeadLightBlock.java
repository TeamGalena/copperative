package galena.coopperative.content.block;

import galena.coopperative.index.CBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class HeadLightBlock extends DirectionalBlock implements CWeatheringCopper {

    private final WeatherState weatherState;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty BROKEN = BooleanProperty.create("broken");

    private static final Integer RANGE = 30;

    private BlockPos spotlightPos = BlockPos.ZERO;


    public HeadLightBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE).setValue(BROKEN, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, BROKEN);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide)
            this.checkIfOn(world, pos, state);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block adjBlock, BlockPos adjPos, boolean p_60514_) {
        if (!world.isClientSide)
            this.checkIfOn(world, pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState clickedState, boolean p_60229_) {
        if (!world.isClientSide)
            this.checkIfOn(world, pos, state);
    }

    @Override
    public void tick(BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull Random random) {
        if (!world.isClientSide)
            this.checkIfOn(world, pos, state);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    private boolean getNeighborSignal(Level world, BlockPos pos, Direction direction) {
        for (Direction directions : Direction.values()) {
            if (directions != direction && world.hasSignal(pos.relative(direction), direction)) {
                return true;
            }
        }
        if (world.hasSignal(pos, Direction.DOWN)) {
            return true;
        }
        BlockPos blockpos = pos.above();

        for(Direction directions : Direction.values()) {
            if (directions != Direction.DOWN && world.hasSignal(blockpos.relative(directions), directions)) {
                return true;
            }
        }
        return false;
    }

    private void checkIfOn(Level world, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        boolean powered = this.getNeighborSignal(world, pos, direction);
        if (powered && !state.getValue(POWERED)) {
            state.setValue(POWERED, true);
            spotlight(world, pos, direction);
            return;
        }
        if (!powered && state.getValue(POWERED)) {
            state.setValue(POWERED, false);
        }
    }

    private boolean spotlight(Level world, BlockPos origin, Direction direction) {
        // Cast until find a solid block
        for (int i = 0; RANGE > i; i++) {
            BlockPos targetPos = origin.relative(direction, i + 1);
            if (!world.getBlockState(targetPos).canOcclude()) {
                moveSpotlight(world, targetPos);
                return true;
            }
        }
        return false;
    }

    private void moveSpotlight(Level world, BlockPos newPos) {
        world.setBlockAndUpdate(newPos, CBlocks.SPOT_LIGHT.get().defaultBlockState());
        if (spotlightPos != newPos && world.getBlockState(spotlightPos).is(CBlocks.SPOT_LIGHT.get()))
            world.removeBlock(spotlightPos, true);
        this.spotlightPos = newPos;
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.getItem().equals(CBlocks.OXIDIZED_OBSERVER.get().asItem()), false);
    }
}
