package galena.coopperative.content.block;

import galena.coopperative.index.CBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

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
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_55669_, BlockPos p_55670_, boolean p_55671_) {
        if (!world.isClientSide) {
            boolean flag = isLit(state);
            if (flag != world.hasNeighborSignal(pos)) {
                if (flag) {
                    world.scheduleTick(pos, this, 4);
                    world.scheduleTick(spotlightPos, this, 4);
                } else {
                    world.setBlock(pos, state.cycle(POWERED), 2);
                    if (world.isEmptyBlock(pos.relative(state.getValue(FACING))))
                        spotlight(state, world, pos);
                }
            }

        }
    }

    private boolean spotlight(BlockState state, Level world, BlockPos pos) {
        if (state.getValue(BROKEN)) return false;
        Direction facing = state.getValue(FACING);
        for (int i = 1; RANGE > i; i++) {
            BlockPos targetPos = pos.relative(facing, i);
            if (!world.isEmptyBlock(targetPos)) {
                spotlightPos = targetPos.relative(facing.getOpposite());
                world.setBlock(spotlightPos, CBlocks.SPOT_LIGHT.get().defaultBlockState(), 2);
                SpotLightBlock spotlight = (SpotLightBlock) world.getBlockState(spotlightPos).getBlock();
                spotlight.setHeadLightPos(pos);
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (isLit(state) && !world.hasNeighborSignal(pos)) {
            world.setBlock(pos, state.cycle(POWERED), 2);
            world.scheduleTick(spotlightPos, this, 4);
        }
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

    public static boolean isLit(BlockState state) {
        return state.getValue(POWERED) && !state.getValue(BROKEN);
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
