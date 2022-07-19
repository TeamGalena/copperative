package galena.coopperative.event;

import galena.coopperative.Coopperative;
import galena.coopperative.registry.CItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Coopperative.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void handleAxeScrapeEvent(BlockEvent.BlockToolModificationEvent event) {
        if (event.isSimulated()) return;
        LevelAccessor world = event.getWorld();
        UseOnContext ctx = event.getContext();
        if (event.getToolAction() == ToolActions.AXE_SCRAPE && WeatheringCopper.getPrevious(event.getState()).isPresent()) {
            ItemStack patina = new ItemStack(CItems.PATINA.get());
            Vec3 dropPos = ctx.getClickLocation();
            world.addFreshEntity(new ItemEntity((Level) world, dropPos.x, dropPos.y, dropPos.z, patina, 0.0D, 0.0D, 0.0D));
        }
    }
}
