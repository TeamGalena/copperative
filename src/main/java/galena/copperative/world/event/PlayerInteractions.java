package galena.copperative.world.event;

import galena.copperative.Copperative;
import galena.copperative.index.CConversions;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Copperative.MOD_ID)
public class PlayerInteractions {

    @SubscribeEvent
    public static void blockToolInteractions(final BlockEvent.BlockToolModificationEvent event) {
        ToolAction action = event.getToolAction();
        BlockState state = event.getState();
        //if (event.isSimulated()) return;

        // Removing Wax ('Unwaxing' - Using an Axe on a waxed block).
        if (action.equals(ToolActions.AXE_WAX_OFF)) {
            CConversions.getUnwaxedVersion(state.getBlock()).ifPresent(block ->
                    event.setFinalState(block.withPropertiesOf(state))
            );
        }

        // Scrape Weathering Block
        if (action.equals(ToolActions.AXE_SCRAPE)) {
            CConversions.getUnweatheredVersion(state.getBlock()).ifPresent(block ->
                    event.setFinalState(block.withPropertiesOf(state))
            );
        }
    }

    /**
     * Use if interaction is not defined in {@link ToolActions}
     **/
    @SubscribeEvent
    public static void blockItemInteractions(final PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        ItemStack itemStack = event.getItemStack();

        // Waxing (Using Honeycomb on a waxable block).
        var waxedBlock = CConversions.getWaxedVersion(state.getBlock());
        if (itemStack.is(Items.HONEYCOMB) && waxedBlock.isPresent()) {

            if (event.getEntity() instanceof ServerPlayer player)
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, itemStack);

            event.getEntity().swing(event.getHand());
            if (!event.getEntity().isCreative()) event.getItemStack().shrink(1);
            if (!world.isClientSide()) world.setBlock(pos, waxedBlock.get().withPropertiesOf(state), 11);
            world.levelEvent(event.getEntity(), 3003, pos, 0);

            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }
}
