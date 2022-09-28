package galena.coopperative.mixin;

import galena.coopperative.content.block.weatheringvanilla.WeatheringDispenserBlock;
import galena.coopperative.content.block.weatheringvanilla.WeatheringDropperBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    @Inject(method = "onRemove", at = @At("HEAD"), cancellable = true)
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean p_52711_, CallbackInfo ci) {
        if (state.getBlock() instanceof WeatheringDispenserBlock || state.getBlock().equals(Blocks.DISPENSER))
            if (newState.getBlock() instanceof WeatheringDispenserBlock || newState.getBlock().equals(Blocks.DISPENSER))
                ci.cancel();

        if (state.getBlock() instanceof WeatheringDropperBlock || state.getBlock().equals(Blocks.DROPPER))
            if (newState.getBlock() instanceof WeatheringDropperBlock || newState.getBlock().equals(Blocks.DROPPER))
                ci.cancel();
    }
}
