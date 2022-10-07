package galena.coopperative.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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

import java.util.Random;
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
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (headLightPos == null) {
            world.removeBlock(pos, false);
            return;
        }
        BlockState headlightState = world.getBlockState(headLightPos);
        if (!(headlightState.getBlock() instanceof HeadLightBlock)) {
            world.removeBlock(pos, false);
            return;
        }
        if (!HeadLightBlock.isLit(headlightState))
            world.removeBlock(pos, false);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    private Direction getRelativeDirection(BlockPos pos1, BlockPos pos2) {
        if (pos1.getX() == pos2.getX()) {
            if (pos1.getY() == pos2.getY()) {
                return pos1.getZ() > pos2.getY() ? Direction.DOWN : Direction.UP;
            }
            return pos1.getY() > pos2.getY() ? Direction.EAST : Direction.WEST;
        }
        return pos1.getX() > pos2.getX() ? Direction.SOUTH : Direction.NORTH;
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

    public void setHeadLightPos(BlockPos pos) {
        this.headLightPos = pos;
    }

    public BlockPos getHeadLightPos() {
        return this.headLightPos;
    }

    protected boolean shouldExist(Level world) {
        BlockState state = world.getBlockState(headLightPos);
        Boolean isHeadlight = state.getBlock() instanceof HeadLightBlock;
        Boolean isLit = HeadLightBlock.isLit(state);
        Boolean isFacingTowardsThis = state.getValue(HeadLightBlock.FACING).getOpposite().equals(this.headLightRelative);

        return isHeadlight && isLit && isFacingTowardsThis;
    }

}
