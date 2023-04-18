package galena.coopperative.data;

import galena.coopperative.Coopperative;
import galena.coopperative.index.CBlocks;
import galena.coopperative.data.provider.CBlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class CBlockStates extends CBlockStateProvider {

    public CBlockStates(DataGenerator gen, ExistingFileHelper help) {
        super(gen, help);
    }

    @Override
    public @NotNull String getName() {
        return Coopperative.MOD_NAME + " Block States";
    }

    @Override
    protected void registerStatesAndModels() {
        block(CBlocks.PATINA_BLOCK);
        weatheringBlock(CBlocks.COPPER_BRICKS);
        weatheringPillarBlock(CBlocks.COPPER_PILLAR);
        weatheringPillarBlock(CBlocks.COPPER_TILES);

        repeater(() -> Blocks.REPEATER);
        repeater(CBlocks.EXPOSED_REPEATER);
        repeater(CBlocks.WEATHERED_REPEATER);
        repeater(CBlocks.OXIDIZED_REPEATER);

        comparator(() -> Blocks.COMPARATOR);
        comparator(CBlocks.EXPOSED_COMPARATOR);
        comparator(CBlocks.WEATHERED_COMPARATOR);
        comparator(CBlocks.OXIDIZED_COMPARATOR);

        piston(() -> Blocks.PISTON);
        piston(CBlocks.EXPOSED_PISTON);
        piston(CBlocks.WEATHERED_PISTON);
        piston(CBlocks.OXIDIZED_PISTON);

        piston(() -> Blocks.STICKY_PISTON);
        piston(CBlocks.EXPOSED_STICKY_PISTON);
        piston(CBlocks.WEATHERED_STICKY_PISTON);
        piston(CBlocks.OXIDIZED_STICKY_PISTON);

        dispenser(() -> Blocks.DISPENSER);
        dispenser(CBlocks.EXPOSED_DISPENSER);
        dispenser(CBlocks.WEATHERED_DISPENSER);
        dispenser(CBlocks.OXIDIZED_DISPENSER);

        dropper(() -> Blocks.DROPPER);
        dropper(CBlocks.EXPOSED_DROPPER);
        dropper(CBlocks.WEATHERED_DROPPER);
        dropper(CBlocks.OXIDIZED_DROPPER);

        observer(() -> Blocks.OBSERVER);
        observer(CBlocks.EXPOSED_OBSERVER);
        observer(CBlocks.WEATHERED_OBSERVER);
        observer(CBlocks.OXIDIZED_OBSERVER);

        lever(() -> Blocks.LEVER);
        lever(CBlocks.EXPOSED_LEVER);
        lever(CBlocks.WEATHERED_LEVER);
        lever(CBlocks.OXIDIZED_LEVER);

        poweredRail(() -> Blocks.POWERED_RAIL);
        poweredRail(CBlocks.EXPOSED_POWERED_RAIL);
        poweredRail(CBlocks.WEATHERED_POWERED_RAIL);
        poweredRail(CBlocks.OXIDIZED_POWERED_RAIL);

        headlight(CBlocks.HEADLIGHT);
        toggler(CBlocks.TOGGLER);

        door(CBlocks.COPPER_DOOR);
        door(CBlocks.EXPOSED_COPPER_DOOR);
        door(CBlocks.WEATHERED_COPPER_DOOR);
        door(CBlocks.OXIDIZED_COPPER_DOOR);

        trapdoor(CBlocks.COPPER_TRAPDOOR);
        trapdoor(CBlocks.EXPOSED_COPPER_TRAPDOOR);
        trapdoor(CBlocks.WEATHERED_COPPER_TRAPDOOR);
        trapdoor(CBlocks.OXIDIZED_COPPER_TRAPDOOR);
    }
}
