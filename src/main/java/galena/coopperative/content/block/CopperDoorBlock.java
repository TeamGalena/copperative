package galena.coopperative.content.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import galena.coopperative.content.index.CBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;
import java.util.function.Supplier;

public class CopperDoorBlock extends DoorBlock implements WeatheringCopper {

    private final WeatherState weatherState;

    public static Supplier<BiMap<DoorBlock, DoorBlock>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<DoorBlock, DoorBlock>builder()
            .put(CBlocks.COPPER_DOOR.get(), CBlocks.EXPOSED_COPPER_DOOR.get())
            .put(CBlocks.EXPOSED_COPPER_DOOR.get(), CBlocks.WEATHERED_COPPER_DOOR.get())
            .put(CBlocks.WEATHERED_COPPER_DOOR.get(), CBlocks.OXIDIZED_COPPER_DOOR.get())
            .build());
    public static final Supplier<BiMap<DoorBlock, DoorBlock>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());


    public CopperDoorBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    private int getCloseSound() {
        return 1011;
    }

    private int getOpenSound() {
        return  1005;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(POWERED)) return InteractionResult.PASS;
        state = state.cycle(OPEN);
        world.setBlock(pos, state, 10);
        world.levelEvent(player, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        world.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).isPresent();
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state));
    }

    public static Optional<BlockState> getPreviousState(BlockState state) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(state.getBlock())).map((block) -> block.withPropertiesOf(state));
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
