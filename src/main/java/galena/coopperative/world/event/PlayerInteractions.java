package galena.coopperative.world.event;

import galena.coopperative.Coopperative;
import galena.coopperative.index.CBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Coopperative.MOD_ID)
public class PlayerInteractions {

    @SubscribeEvent
    public static void blockToolInteractions(final BlockEvent.BlockToolModificationEvent event) {
        ToolAction action = event.getToolAction();
        BlockState state = event.getState();
        if (event.isSimulated()) return;

        // Removing Wax ('Unwaxing' - Using an Axe on a waxed block).
        if (action.equals(ToolActions.AXE_WAX_OFF)) {
            Block unWaxedBlock = CBlocks.WAXED_BLOCKS.get(state.getBlock());
            if (unWaxedBlock == null) return;
            event.setFinalState(unWaxedBlock.withPropertiesOf(state));
        }

        // Scrape Weathering Block
        if (action.equals(ToolActions.AXE_SCRAPE)) {
            Block prevBlock = Coopperative.WEATHERING_BLOCKS.get().inverse().get(state.getBlock());
            if (prevBlock == null) return;
            event.setFinalState(prevBlock.withPropertiesOf(state));
        }
    }

    /**
     *Use if interaction is not defined in {@link ToolActions}
     **/
    @SubscribeEvent
    public static void blockItemInteractions(final PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        ItemStack itemStack = event.getItemStack();

        // Waxing (Using Honeycomb on a waxable block).
        if (itemStack.is(Items.HONEYCOMB) && CBlocks.WAXED_BLOCKS.inverse().get(state.getBlock()) != null) {

            if (event.getEntity() instanceof ServerPlayer player) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, itemStack);

            event.getPlayer().swing(event.getHand());
            if (!event.getPlayer().isCreative()) event.getItemStack().shrink(1);
            Block waxedBlock = CBlocks.WAXED_BLOCKS.inverse().get(state.getBlock());
            if (!world.isClientSide() && waxedBlock != null) world.setBlock(pos, waxedBlock.withPropertiesOf(state), 11);
            world.levelEvent(event.getPlayer(), 3003, pos, 0);
        }
    }
}
