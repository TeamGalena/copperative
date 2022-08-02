package galena.coopperative.content.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import galena.coopperative.content.index.CBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class HeadLightBlock extends DirectionalBlock implements WeatheringCopper {

    private final WeatherState weatherState;
    public static Supplier<BiMap<DoorBlock, DoorBlock>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<DoorBlock, DoorBlock>builder()

            .build());
    public static final Supplier<BiMap<DoorBlock, DoorBlock>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty BROKEN = BooleanProperty.create("broken");

    private static final Integer RANGE = 64;


    public HeadLightBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.FALSE).setValue(BROKEN, Boolean.FALSE));
        this.weatherState = weatherState;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, BROKEN);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
        if (!state.getValue(BROKEN) && !world.hasNeighborSignal(pos)) {
            world.setBlock(pos, state.cycle(LIT), 2);
            spotLight(world, state, pos);
        }
    }

    private void spotLight(ServerLevel world, BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        if (!world.getBlockState(pos.relative(facing)).isAir()) return;
        for (int i = 0; RANGE > i; i++) {
            if (!world.getBlockState(pos.relative(facing, i + 1)).isAir()) {
                world.setBlockAndUpdate(pos.relative(facing, i), Blocks.LIGHT.defaultBlockState());
                return;
            }
        }
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state));
    }

    public static Optional<BlockState> getPreviousState(BlockState state) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
