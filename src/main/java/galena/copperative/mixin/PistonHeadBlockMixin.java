package galena.copperative.mixin;

import galena.copperative.content.block.weatheringvanilla.WeatheringPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {
    @Inject(method = "isFittingBase", at = @At(value = "RETURN"), cancellable = true)
    private void isFittingBase(BlockState state, BlockState adjState, CallbackInfoReturnable<Boolean> cir) {
        boolean isCopperPistonBlock = adjState.getBlock() instanceof WeatheringPistonBlock;
        if (!cir.getReturnValue())
            cir.setReturnValue(isCopperPistonBlock
                && adjState.getValue(PistonBaseBlock.EXTENDED)
                && adjState.getValue(PistonBaseBlock.FACING) == state.getValue(PistonHeadBlock.FACING)
            );
    }
}
