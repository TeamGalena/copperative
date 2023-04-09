package galena.coopperative.content.block;

import galena.coopperative.Coopperative;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SolderingTableBlock extends Block {

    private static final Component CONTAINER_TITLE = Component.translatable(Coopperative.MOD_ID,"container.soldering");

    public SolderingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult context) {
        if (world.isClientSide)
            return InteractionResult.SUCCESS;

        player.openMenu(blockState.getMenuProvider(world, blockPos));
        //player.awardStat();
        return InteractionResult.CONSUME;
    }

    public MenuProvider getMenuProvider(BlockState blockState, Level world, BlockPos blockPos) {
        return new SimpleMenuProvider(
                (id, inventory, player) -> new CraftingMenu(id, inventory, ContainerLevelAccess.create(world, blockPos)),
                CONTAINER_TITLE
        );
    }
}
