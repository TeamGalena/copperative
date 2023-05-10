package galena.coopperative.content.item;

import galena.coopperative.index.CConversions;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PatinaItem extends Item {

    public PatinaItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        if (ctx.getPlayer() == null) return super.useOn(ctx);
        Level world = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockState state = world.getBlockState(ctx.getClickedPos());
        Block block = state.getBlock();
        ItemStack stack = ctx.getItemInHand();
        InteractionHand hand = ctx.getHand();
        BlockPos pos = ctx.getClickedPos();
        if (block instanceof WeatheringCopper && WeatheringCopper.getNext(block).isPresent()) {
            Block nextWeatherBlock = WeatheringCopper.getNext(block).get();
            return apply(world, player, stack, hand, pos, state, nextWeatherBlock);
        }

        return CConversions.getWeatheredVersion(block).map(weathered ->
                apply(world, player, stack, hand, pos, state, weathered)
        ).orElseGet(() ->
            super.useOn(ctx)
        );
    }

    private InteractionResult apply(Level world, Player player, ItemStack stack, InteractionHand hand, BlockPos pos, BlockState state, Block newBlock) {
        player.swing(hand);
        world.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
        world.levelEvent(player, 3005, pos, 0);
        world.setBlock(pos, newBlock.withPropertiesOf(state), 11);

        if (!player.isCreative())
            stack.shrink(1);
        if (player instanceof ServerPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, stack);
        return InteractionResult.CONSUME;
    }
}
