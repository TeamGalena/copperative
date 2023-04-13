package galena.coopperative.content.block;

import galena.coopperative.content.block.tile.HeadlightTile;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HeadLightBlock extends DirectionalBlock implements CWeatheringCopper, EntityBlock {

    private final WeatherState weatherState;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty BROKEN = BooleanProperty.create("broken");

    public static final int RANGE = 30;

    private Block getLight() {
        return CBlocks.SPOT_LIGHT.get();
    }

    public HeadLightBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE).setValue(BROKEN, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, BROKEN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HeadlightTile(pos, state);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbor, boolean isMoving) {
        if (!world.isClientSide) {
            boolean flag = isLit(state);
            if (flag != world.hasNeighborSignal(pos)) {
                if (flag) {
                    world.scheduleTick(pos, this, 4);
                } else {
                    world.setBlock(pos, state.cycle(POWERED), 2);
                    spotlight(state, world, pos);
                }
            }
        }
    }

    private static Optional<HeadlightTile> getTile(LevelAccessor world, BlockPos pos) {
        return world.getBlockEntity(pos, CBlocks.HEADLIGHT_TILE.get());
    }

    private void spotlight(BlockState state, LevelAccessor world, BlockPos pos) {
        if (state.getValue(BROKEN)) return;
        getTile(world, pos).ifPresent(HeadlightTile::createSpotlight);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (isLit(state) && !world.hasNeighborSignal(pos)) {
            world.setBlock(pos, state.cycle(POWERED), 2);
            getTile(world, pos).ifPresent(HeadlightTile::extinguishSpotlight);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (type == CBlocks.HEADLIGHT_TILE.get() && isLit(state)) {
            return (BlockEntityTicker<T>) createTicker();
        } else {
            return null;
        }
    }

    private BlockEntityTicker<HeadlightTile> createTicker() {
        return (world, pos, state, tile) -> tile.checkOcclusion();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    //@Override
    //public BlockState rotate(BlockState state, Rotation rotation) {
    //    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    //}

    //@Override
    //public BlockState mirror(BlockState state, Mirror mirror) {
    //    return state.rotate(mirror.getRotation(state.getValue(FACING)));
    //}

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);
        getTile(world, pos).ifPresent(HeadlightTile::extinguishSpotlight);
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
