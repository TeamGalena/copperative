package galena.copperative.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.function.ToIntFunction;

public class SpotLightBlock extends AirBlock implements SimpleWaterloggedBlock {

    public static final int MAX_LEVEL = 15;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final ToIntFunction<BlockState> LIGHT_EMISSION = (state) -> state.getValue(LEVEL);

    public SpotLightBlock(Properties properties) {
        super(properties.replaceable());
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, MAX_LEVEL).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        //if (state.getFluidState().isEmpty()) return RenderShape.INVISIBLE;
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, WATERLOGGED);
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

    public BlockState createState(LevelAccessor world, BlockPos pos) {
        if (world.getFluidState(pos).is(Fluids.WATER)) {
            return defaultBlockState().setValue(WATERLOGGED, true);
        } else {
            return defaultBlockState();
        }
    }

}
