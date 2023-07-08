package galena.coopperative.data;

import galena.coopperative.client.DynamicCooperativeResourcePack;
import galena.coopperative.data.provider.CBlockStateProvider;
import galena.coopperative.index.CBlocks;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.io.IOException;
import java.nio.file.Path;

public class CBlockStateOverwrites extends CBlockStateProvider {

    public CBlockStateOverwrites(DataGenerator gen, ExistingFileHelper help) {
        super(gen, help);
    }

    @Override
    protected void registerStatesAndModels() {
        repeater(() -> Blocks.REPEATER);
        comparator(() -> Blocks.COMPARATOR);
        piston(() -> Blocks.PISTON);
        piston(() -> Blocks.STICKY_PISTON);
        dispenser(() -> Blocks.DISPENSER);
        dropper(() -> Blocks.DROPPER);
        observer(() -> Blocks.OBSERVER);
        lever(() -> Blocks.LEVER);
        poweredRail(() -> Blocks.POWERED_RAIL);

        CBlocks.EXPOSERS.all().forEach(this::exposer);
        CBlocks.RELAYERS.all().forEach(this::relayer);
        CBlocks.CRANKS.all().forEach(this::crank);
        CBlocks.COG_BLOCKS.weathered().forEach(this::cogBlock);
        CBlocks.RANDOMIZERS.all().forEach(this::randomizer);
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        super.run((path, bytes, hash) -> {
            var newPath = DynamicCooperativeResourcePack.OVERRIDDEN_NAMESPACES.stream().reduce(path.toString(), (it, namespace) ->
                    it.replace(namespace + "\\blockstates", DynamicCooperativeResourcePack.NAMESPACE + "\\blockstates")
            );
            cache.writeIfNeeded(Path.of(newPath), bytes, hash);
        });
    }
}
