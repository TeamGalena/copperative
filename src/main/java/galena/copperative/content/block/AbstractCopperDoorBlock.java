package galena.copperative.content.block;

import galena.copperative.index.CBlocks;
import galena.copperative.index.CConversions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AbstractCopperDoorBlock extends DoorBlock {

    public static BlockSetType blockSetFor(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED, EXPOSED -> CBlocks.COPPER_BLOCK_SET;
            default -> CBlocks.WEATHERED_COPPER_BLOCK_SET;
        };
    }

    public AbstractCopperDoorBlock(Properties properties, BlockSetType blockSetType) {
        super(properties, blockSetType);
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
