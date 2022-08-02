package galena.coopperative.content.event;

import galena.coopperative.Coopperative;
import galena.coopperative.content.index.CItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Coopperative.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void handleAxeScrapeEvent(BlockEvent.BlockToolModificationEvent event) {
        if (event.isSimulated() || event.getContext() == null) return;
        Level world = (Level) event.getWorld();
        UseOnContext ctx = event.getContext();
        if (event.getToolAction() == ToolActions.AXE_SCRAPE && WeatheringCopper.getPrevious(event.getState()).isPresent()) {
            ItemStack patina = new ItemStack(CItems.PATINA.get());
            BlockPos dropPos = ctx.getClickedPos();
            Direction clickedFace = ctx.getClickedFace();
            Block.popResourceFromFace(world, dropPos, clickedFace,  patina);
        }
    }
}
