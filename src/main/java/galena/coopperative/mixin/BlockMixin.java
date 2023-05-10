package galena.coopperative.mixin;

import galena.coopperative.config.CommonConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
    public void modifySoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
        if (CommonConfig.getPossibleOverwrites().contains(state.getBlock())) {
            cir.setReturnValue(SoundType.COPPER);
        }
    }
}
