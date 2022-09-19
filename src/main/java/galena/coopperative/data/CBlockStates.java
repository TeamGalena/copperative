package galena.coopperative.data;

import galena.coopperative.Coopperative;
import galena.coopperative.index.CBlocks;
import galena.coopperative.data.provider.CBlockStateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CBlockStates extends CBlockStateProvider {

    public CBlockStates(DataGenerator gen, ExistingFileHelper help) {
        super(gen, help);
    }

    @Override
    public String getName() {
        return Coopperative.MOD_NAME + " Block States";
    }

    @Override
    protected void registerStatesAndModels() {
        block(CBlocks.PATINA_BLOCK);
        weatheringBlock(CBlocks.COPPER_BRICKS);
        weatheringPillarBlock(CBlocks.COPPER_PILLAR);
        copperTilesBlock(CBlocks.COPPER_TILES);

        observer(() -> Blocks.OBSERVER);
        observer(CBlocks.EXPOSED_OBSERVER);
        observer(CBlocks.WEATHERED_OBSERVER);
        observer(CBlocks.OXIDIZED_OBSERVER);
        observer(CBlocks.WAXED_OBSERVER, () -> Blocks.OBSERVER);
        observer(CBlocks.WAXED_EXPOSED_OBSERVER, CBlocks.EXPOSED_OBSERVER);
        observer(CBlocks.WAXED_WEATHERED_OBSERVER, CBlocks.WEATHERED_OBSERVER);
        observer(CBlocks.WAXED_OXIDIZED_OBSERVER, CBlocks.OXIDIZED_OBSERVER);

        //headlight(CBlocks.HEADLIGHT);

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
