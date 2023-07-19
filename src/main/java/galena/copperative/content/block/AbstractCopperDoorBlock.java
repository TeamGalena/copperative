package galena.copperative.content.block;

import galena.copperative.index.CConversions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AbstractCopperDoorBlock extends DoorBlock {

    public static final Material SOFT_METAL = new Material.Builder(MaterialColor.METAL).build();
    private final boolean canBeUsedByPlayers;

    public static Material materialFor(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED, EXPOSED -> SOFT_METAL;
            default -> Material.METAL;
        };
    }

    public static Properties propertiesFor(WeatheringCopper.WeatherState state) {
        return Properties.of(materialFor(state), CWeatheringCopper.colorFor(state)).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.COPPER).noOcclusion();
    }

    public AbstractCopperDoorBlock(Properties properties) {
        super(properties);
        this.canBeUsedByPlayers = material != Material.METAL;
    }

    private int getCloseSound() {
        return 1011;
    }

    private int getOpenSound() {
        return 1005;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!canBeUsedByPlayers) return InteractionResult.PASS;
        state = state.cycle(OPEN);
        world.setBlock(pos, state, 10);
        world.levelEvent(player, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        world.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_52779_, BlockPos adjPos, boolean p_52781_) {
        if (!world.isClientSide) {
            var otherState = world.getBlockState(adjPos);
            if (otherState.getBlock() instanceof AbstractCopperDoorBlock) updateOtherHalf(otherState, world, adjPos);
        }
        super.neighborChanged(state, world, pos, p_52779_, adjPos, p_52781_);
    }

    protected void updateOtherHalf(BlockState state, Level world, BlockPos pos) {
        BlockPos otherHalfPos = state.getValue(HALF).equals(DoubleBlockHalf.UPPER) ? pos.below() : pos.above();
        BlockState otherHalfState = world.getBlockState(otherHalfPos);

        if (otherHalfState.is(state.getBlock())) return;

        var waxed = CConversions.getWaxedVersion(state.getBlock());
        var unwaxed = CConversions.getUnwaxedVersion(state.getBlock());

        if (waxed.filter(otherHalfState::is).isPresent()) {
            world.levelEvent(3004, otherHalfPos, 0);
        } else if (unwaxed.filter(otherHalfState::is).isPresent()) {
            world.levelEvent(3003, otherHalfPos, 0);
        } else {
            world.levelEvent(3005, otherHalfPos, 0);
        }

        var newState = state.getBlock().withPropertiesOf(otherHalfState);
        world.setBlock(otherHalfPos, newState, 2);
    }
}
