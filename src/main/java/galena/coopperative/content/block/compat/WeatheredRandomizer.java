package galena.coopperative.content.block.compat;

import galena.coopperative.content.block.CWeatheringCopper;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import vazkii.quark.content.automation.base.RandomizerPowerState;

import javax.annotation.Nonnull;
import java.util.EnumSet;

import static vazkii.quark.content.automation.block.RedstoneRandomizerBlock.FACING;
import static vazkii.quark.content.automation.block.RedstoneRandomizerBlock.POWERED;

public class WeatheredRandomizer extends Block implements CWeatheringCopper {

    public static Block loadUnaffected() {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("quark", "redstone_randomizer"));
    }

    private final WeatherState weatherState;

    public WeatheredRandomizer(WeatherState weatherState) {
        super(Properties.of(Material.DECORATION).strength(0.0F).sound(SoundType.WOOD));
        this.weatherState = weatherState;
    }

    @Override
    public WeatherState getAge() {
        return weatherState;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab tab, @NotNull NonNullList<ItemStack> items) {
        insert(this, false, items, itemStack -> itemStack.is(ModRegistry.RELAYER.get().asItem()), true);
    }

    /**
     * The following code is taken from {@link vazkii.quark.content.automation.block.RedstoneRandomizerBlock}`
     */
    public static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        boolean isPowered = this.isPowered(state);
        boolean willBePowered = this.shouldBePowered(world, pos, state);
        if (isPowered != willBePowered) {
            if (!willBePowered) {
                state = (BlockState)state.setValue(POWERED, RandomizerPowerState.OFF);
            } else {
                state = (BlockState)state.setValue(POWERED, rand.nextBoolean() ? RandomizerPowerState.LEFT : RandomizerPowerState.RIGHT);
            }

            world.setBlockAndUpdate(pos, state);
        }

    }

    protected void updateState(Level world, BlockPos pos, BlockState state) {
        boolean isPowered = this.isPowered(state);
        boolean willBePowered = this.shouldBePowered(world, pos, state);
        if (isPowered != willBePowered && !world.getBlockTicks().willTickThisTick(pos, this)) {
            TickPriority priority = isPowered ? TickPriority.VERY_HIGH : TickPriority.HIGH;
            world.scheduleTick(pos, this, 2, priority);
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, POWERED});
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader world, BlockPos pos) {
        return canSupportRigidBlock(world, pos.below());
    }

    protected boolean isPowered(BlockState state) {
        return state.getValue(POWERED) != RandomizerPowerState.OFF;
    }

    public int getDirectSignal(BlockState blockState, @Nonnull BlockGetter blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
        return blockState.getSignal(blockAccess, pos, side);
    }

    public int getSignal(BlockState blockState, @Nonnull BlockGetter blockAccess, @Nonnull BlockPos pos, @Nonnull Direction side) {
        var powerState = blockState.getValue(POWERED);

        return switch (powerState) {
            case RIGHT -> blockState.getValue(FACING).getClockWise() == side ? 15 : 0;
            case LEFT -> blockState.getValue(FACING).getCounterClockWise() == side ? 15 : 0;
            default -> 0;
        };
    }

    public void neighborChanged(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (state.canSurvive(world, pos)) {
            this.updateState(world, pos, state);
        } else {
            breakAndDrop(this, state, world, pos);
        }
    }

    public static void breakAndDrop(Block block, BlockState state, Level world, BlockPos pos) {
        dropResources(state, world, pos, (BlockEntity)null);
        world.removeBlock(pos, false);
        for (Direction direction : Direction.values()) {
            world.updateNeighborsAt(pos.relative(direction), block);
        }
    }

    protected boolean shouldBePowered(Level world, BlockPos pos, BlockState state) {
        return this.calculateInputStrength(world, pos, state) > 0;
    }

    protected int calculateInputStrength(Level world, BlockPos pos, BlockState state) {
        var face = state.getValue(FACING);
        var checkPos = pos.relative(face);
        int strength = world.getSignal(checkPos, face);
        if (strength >= 15) {
            return strength;
        } else {
            var checkState = world.getBlockState(checkPos);
            return Math.max(strength, checkState.getBlock() == Blocks.REDSTONE_WIRE ? (Integer)checkState.getValue(RedStoneWireBlock.POWER) : 0);
        }
    }

    public boolean isSignalSource(@Nonnull BlockState state) {
        return true;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        if (this.shouldBePowered(world, pos, state)) {
            world.scheduleTick(pos, this, 1);
        }

    }

    public void onPlace(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        notifyNeighbors(this, world, pos, state);
    }

    public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!isMoving && state.getBlock() != newState.getBlock()) {
            super.onRemove(state, world, pos, newState, false);
            notifyNeighbors(this, world, pos, state);
        }
    }

    public static void notifyNeighbors(Block block, Level world, BlockPos pos, BlockState state) {
        var face = state.getValue(FACING);
        BlockPos neighborPos = pos.relative(face.getOpposite());
        if (!ForgeEventFactory.onNeighborNotify(world, pos, world.getBlockState(pos), EnumSet.of(face.getOpposite()), false).isCanceled()) {
            world.neighborChanged(neighborPos, block, pos);
            world.updateNeighborsAtExceptFromFacing(neighborPos, block, face);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (stateIn.getValue(POWERED) != RandomizerPowerState.OFF) {
            double x = (double)pos.getX() + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
            double y = (double)pos.getY() + 0.4 + ((double)rand.nextFloat() - 0.5) * 0.2;
            double z = (double)pos.getZ() + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
            worldIn.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0, 0.0, 0.0);
        }
    }
}
