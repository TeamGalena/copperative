package galena.coopperative.content.block.tile;

import galena.coopperative.content.block.HeadLightBlock;
import galena.coopperative.index.CBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HeadlightTile extends BlockEntity {

    private @Nullable BlockPos savedSpotlight;

    public HeadlightTile(BlockPos pos, BlockState state) {
        super(CBlocks.HEADLIGHT_TILE.get(), pos, state);
    }

    public void checkOcclusion() {
        if (!hasLevel()) return;
        if (level.getGameTime() % 10 != 0) return;

        var currentTarget = findTarget();
        var different = currentTarget.isEmpty() || !currentTarget.get().equals(savedSpotlight);

        if (different) {
            if (savedSpotlight != null) extinguishSpotlight();
            currentTarget.ifPresent(this::placeSpotlight);
        }
    }

    public void createSpotlight() {
        if (!hasLevel()) return;

        var targetPos = findTarget();
        targetPos.ifPresent(this::placeSpotlight);
    }

    private void placeSpotlight(BlockPos at) {
        level.setBlock(at, CBlocks.SPOT_LIGHT.get().createState(level, at), 2);
        saveSpotlight(at);
    }

    public void extinguishSpotlight() {
        if (!hasLevel()) return;

        getSavedSpotlight().ifPresent(targetPos -> {
            var spotlight = level.getBlockState(targetPos);
            if (spotlight.is(CBlocks.SPOT_LIGHT.get())) {
                level.removeBlock(targetPos, false);
                forgetSpotlight();
            }
        });
    }

    private Optional<BlockPos> findTarget() {
        Direction facing = getBlockState().getValue(HeadLightBlock.FACING);

        BlockPos openSpace = null;

        for (int i = 1; HeadLightBlock.RANGE > i; i++) {
            BlockPos targetPos = getBlockPos().relative(facing, i);
            var state = level.getBlockState(targetPos);
            if(isReplaceable(state)) openSpace = targetPos;
            if (!shinesThrough(state)) {
                return Optional.ofNullable(openSpace);
            }
        }

        return Optional.empty();
    }

    private boolean shinesThrough(BlockState state) {
        return state.isAir() || !state.canOcclude();
    }

    private boolean isReplaceable(BlockState state) {
        if (state.isAir() || state.is(CBlocks.SPOT_LIGHT.get())) return true;
        if(state.is(Blocks.WATER)) return state.getFluidState().isSource();
        return false;
    }

    public Optional<BlockPos> getSavedSpotlight() {
        return Optional.ofNullable(savedSpotlight);
    }

    private void saveSpotlight(BlockPos pos) {
        savedSpotlight = pos;
    }

    private void forgetSpotlight() {
        savedSpotlight = null;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        extinguishSpotlight();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("ActiveSpotlight")) {
            savedSpotlight = NbtUtils.readBlockPos(nbt.getCompound("ActiveSpotlight"));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        var nbt = super.serializeNBT();
        if (savedSpotlight != null) {
            nbt.put("ActiveSpotlight", NbtUtils.writeBlockPos(savedSpotlight));
        }
        return nbt;
    }
}
