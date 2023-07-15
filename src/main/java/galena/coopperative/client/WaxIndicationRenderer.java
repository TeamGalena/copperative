package galena.coopperative.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.AbstractObject2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class WaxIndicationRenderer {

    private static final ItemStack AXE = new ItemStack(Items.IRON_AXE);

    private static final AbstractObject2BooleanMap<BlockState> CACHE = new Object2BooleanLinkedOpenHashMap<>();

    private static boolean isWaxed(BlockState state, UseOnContext context) {
        return CACHE.computeIfAbsent(state, (BlockState it) -> {
                    var result = state.getToolModifiedState(context, ToolActions.AXE_WAX_OFF, true);
                    return result != null && result != it;
                }
        );
    }

    private static void animateIn(int range, ClientLevel level, Player player, BlockPos.MutableBlockPos pos, UseOnContext context) {
        for (int i = 0; i < 667; ++i) {
            int x = player.getBlockX() + level.random.nextInt(range) - level.random.nextInt(range);
            int y = player.getBlockY() + level.random.nextInt(range) - level.random.nextInt(range);
            int z = player.getBlockZ() + level.random.nextInt(range) - level.random.nextInt(range);
            pos.set(x, y, z);

            var state = level.getBlockState(pos);

            if (isWaxed(state, context)) {
                var side = level.random.nextInt(6);
                var r1 = level.random.nextDouble();
                var r2 = level.random.nextDouble();
                switch (side) {
                    case 0 -> level.addParticle(ParticleTypes.WAX_ON, x + 1.1, y + r1, z + r2, 0.0, 0.0, 0.0);
                    case 1 -> level.addParticle(ParticleTypes.WAX_ON, x + r1, y + 1.1, z + r2, 0.0, 0.0, 0.0);
                    case 2 -> level.addParticle(ParticleTypes.WAX_ON, x + r1, y + r2, z + 1.1, 0.0, 0.0, 0.0);
                    case 3 -> level.addParticle(ParticleTypes.WAX_ON, x - 0.1, y + r1, z + r2, 0.0, 0.0, 0.0);
                    case 4 -> level.addParticle(ParticleTypes.WAX_ON, x + r1, y - 0.1, z + r2, 0.0, 0.0, 0.0);
                    case 5 -> level.addParticle(ParticleTypes.WAX_ON, x + r1, y + r2, z - 0.1, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        var mc = Minecraft.getInstance();
        var level = mc.level;
        var player = mc.player;

        if (level == null || player == null) return;
        if (!player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.HONEYCOMB)) return;

        CACHE.clear();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        var context = new UseOnContext(level, player, InteractionHand.MAIN_HAND, AXE, BlockHitResult.miss(Vec3.ZERO, Direction.DOWN, pos));

        animateIn(16, level, player, pos, context);
        animateIn(32, level, player, pos, context);

    }

    //@SubscribeEvent
    public static void renderLast(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) return;

        var mc = Minecraft.getInstance();
        var bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());
        var pos = new BlockPos(-3, -61, -3);
        var shape = Shapes.create(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        LevelRenderer.renderVoxelShape(event.getPoseStack(), vertexConsumer, shape,
                pos.getX() - mc.player.getX(),
                pos.getY() - mc.player.getEyeY(),
                pos.getZ() - mc.player.getZ(),
                1F, 0F, 1F, 1F);
    }

}
