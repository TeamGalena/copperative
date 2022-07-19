package galena.coopperative.block;

import galena.coopperative.registry.CBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.List;
import java.util.Optional;

public class WeatheringCopperObserverBlock extends DirectionalBlock implements WeatheringCopper {
    private final WeatheringCopper.WeatherState weatherState;

    public static final List<? extends Block> WeatherStates = List.of(
            CBlocks.OBSERVER.get(),
            CBlocks.EXPOSED_OBSERVER.get(),
            CBlocks.WEATHERED_OBSERVER.get(),
            CBlocks.OXIDIZED_OBSERVER.get()
    );

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public WeatheringCopperObserverBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(properties);
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(POWERED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource p_221843_) {
        if (state.getValue(POWERED)) {
            world.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), 2);
        } else {
            world.setBlock(pos, state.setValue(POWERED, Boolean.TRUE), 2);
            world.scheduleTick(pos, this, 2);
        }

        this.updateNeighborsInFront(world, pos, state);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState adjState, LevelAccessor world, BlockPos pos, BlockPos adjPos) {
        if (state.getValue(FACING) == direction && !state.getValue(POWERED)) {
            this.startSignal(world, pos);
        }

        return super.updateShape(state, direction, adjState, world, pos, adjPos);
    }

    private void startSignal(LevelAccessor world, BlockPos pos) {
        if (!world.isClientSide() && !world.getBlockTicks().hasScheduledTick(pos, this)) {
            world.scheduleTick(pos, this, 2);
        }

    }

    protected void updateNeighborsInFront(Level world, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos frontPos = pos.relative(direction.getOpposite());
        world.neighborChanged(frontPos, this, pos);
        world.updateNeighborsAtExceptFromFacing(frontPos, this, direction);
    }

    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getSignal(world, pos, direction);
    }

    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) && state.getValue(FACING) == direction ? 15 : 0;
    }

    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState adjState, boolean isMoving) {
        if (!state.is(adjState.getBlock())) {
            if (!world.isClientSide() && state.getValue(POWERED) && !world.getBlockTicks().hasScheduledTick(pos, this)) {
                BlockState blockstate = state.setValue(POWERED, Boolean.FALSE);
                world.setBlock(pos, blockstate, 18);
                this.updateNeighborsInFront(world, pos, blockstate);
            }

        }
    }

    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState adjState, boolean isMoving) {
        if (!state.is(adjState.getBlock())) {
            if (!world.isClientSide && state.getValue(POWERED) && world.getBlockTicks().hasScheduledTick(pos, this)) {
                this.updateNeighborsInFront(world, pos, state.setValue(POWERED, Boolean.FALSE));
            }

        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
    }

    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        this.onRandomTick(state, world, pos, random);
    }

    /*
    Weathering Stuff
     */

    public static Block getFirst() {
        return WeatherStates.get(1);
    }

    public static Optional<Block> getPrevious(Block block) {
        int index = WeatherStates.indexOf(block);
        return Optional.ofNullable(WeatherStates.get(index - 1));
    }

    public static Optional<Block> getNext(Block block) {
        int index = WeatherStates.indexOf(block);
        return Optional.ofNullable(WeatherStates.get(index + 1));
    }

    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }
}