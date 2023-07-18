package galena.copperative.content.block;

import galena.copperative.index.CBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class TogglerBlock extends DiodeBlock implements CWeatheringCopper {

    public final WeatherState weatherState;

    public static BooleanProperty POWERING = BooleanProperty.create("powering");

    public TogglerBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
        this.registerDefaultState(this.defaultBlockState().setValue(POWERING, false).setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, POWERING, FACING);
    }

    public int getSignal(BlockState blockState, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return blockState.getValue(FACING) == side ? this.getOutputSignal(blockAccess, pos, blockState) : 0;
    }

    @Override
    protected int getDelay(@NotNull BlockState state) {
        return 1;
    }

    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!player.mayBuild())
            return InteractionResult.PASS;
        if (player.isShiftKeyDown())
            return InteractionResult.PASS;

        return this.activated(world, pos, state);
    }

    @Override
    protected int getOutputSignal(@NotNull BlockGetter world, @NotNull BlockPos pos, BlockState state) {
        return state.getValue(POWERING) ? 15 : 0;
    }

    @Override
    public void tick(BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        boolean poweredPreviously = state.getValue(POWERED);
        super.tick(state, world, pos, random);
        BlockState newState = world.getBlockState(pos);
        if (newState.getValue(POWERED) && !poweredPreviously) {
            this.activated(world, pos, newState);
        }

    }

    protected InteractionResult activated(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide) {
            float pitch = !(Boolean) state.getValue(POWERING) ? 1.0F : 0.6F;
            world.playSound(null, pos, SoundEvents.AMETHYST_CLUSTER_PLACE, SoundSource.BLOCKS, 0.3F, pitch);
            world.setBlock(pos, state.cycle(POWERING), 2);
        }

        return InteractionResult.SUCCESS;
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        if (side == null)
            return false;

        return side.getAxis() == state.getValue(FACING).getAxis();
    }

    private static void makeParticle(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction direction = state.getValue(FACING).getOpposite();
        double x = pos.getX() + 0.5D - 0.1D * direction.getStepX();
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D - 0.1D * direction.getStepZ();
        level.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 0.7F), x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERING) && random.nextFloat() < 0.25F) {
            makeParticle(state, level, pos);
        }
    }

    @Override
    public @NotNull WeatherState getAge() {
        return weatherState;
    }
}
