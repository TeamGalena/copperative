package galena.copperative.content.event;

import galena.copperative.Copperative;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Copperative.MOD_ID)
public class CommonEvents {

    public static final ResourceLocation PATINA_LOOT_TABLE = new ResourceLocation(Copperative.MOD_ID, "gameplay/patina");

    @SubscribeEvent
    public static void handleAxeScrapeEvent(BlockEvent.BlockToolModificationEvent event) {
        UseOnContext ctx = event.getContext();
        if (event.isSimulated() || ctx == null) return;
        if (event.getPlayer() != null && event.getPlayer().isCreative()) return;
        if (!(event.getLevel() instanceof ServerLevel world)) return;
        if (event.getToolAction() == ToolActions.AXE_SCRAPE && WeatheringCopper.getPrevious(event.getState()).isPresent()) {
            var dropPos = ctx.getClickedPos();
            var clickedFace = ctx.getClickedFace();
            var lootTable = world.getServer().getLootData().getLootTable(PATINA_LOOT_TABLE);
            var lootContext = new LootParams.Builder(world)
                    .withParameter(LootContextParams.BLOCK_STATE, event.getState())
                    .withParameter(LootContextParams.ORIGIN, ctx.getClickLocation())
                    .withParameter(LootContextParams.TOOL, ctx.getItemInHand())
                    .withParameter(LootContextParams.THIS_ENTITY, event.getPlayer())
                    .create(LootContextParamSets.BLOCK);

            lootTable.getRandomItems(lootContext).forEach(stack ->
                    Block.popResourceFromFace(world, dropPos, clickedFace, stack)
            );
        }
    }
}
