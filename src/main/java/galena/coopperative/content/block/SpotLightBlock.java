package galena.coopperative.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

public class SpotLightBlock extends AirBlock implements SimpleWaterloggedBlock {

    public static final int MAX_LEVEL = 15;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final ToIntFunction<BlockState> LIGHT_EMISSION = (state) -> state.getValue(LEVEL);
    public BlockPos headLightPos;
    public Direction headLightRelative;

    public SpotLightBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 15).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, WATERLOGGED);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState adjState, boolean isMoving) {
        super.onPlace(state, world, pos, adjState, isMoving);
        if (shouldExist(world)) world.setBlock(pos, this.getFluidState(state).createLegacyBlock(), 2);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState adjState, LevelAccessor world, BlockPos pos, BlockPos adjPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return super.updateShape(state, direction, adjState, world, pos, adjPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public BlockPos getHeadLightPos() {
        return this.headLightPos;
    }

    protected boolean shouldExist(Level world) {
        BlockState state = world.getBlockState(headLightPos);
        Boolean isHeadlight = state.getBlock() instanceof HeadLightBlock;
        Boolean isLit = state.getValue(HeadLightBlock.POWERED);
        Boolean isFacingTowardsThis = state.getValue(HeadLightBlock.FACING).getOpposite().equals(this.headLightRelative);

        return isHeadlight && isLit && isFacingTowardsThis;
    }

}
