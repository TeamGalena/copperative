package galena.coopperative.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static galena.coopperative.Coopperative.CopperizedBlocks;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
    public void modifySoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
        if (CopperizedBlocks.contains(state.getBlock())) {
            cir.setReturnValue(SoundType.COPPER);
        }
    }
}
