package galena.copperative.data;

import galena.copperative.client.DynamicCopperativeResourcePack;
import galena.copperative.data.provider.CBlockStateProvider;
import galena.copperative.index.CBlocks;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<?> run(CachedOutput cache) {
        return super.run((path, bytes, hash) -> {
            var newPath = DynamicCopperativeResourcePack.OVERRIDDEN_NAMESPACES.stream().reduce(path.toString(), (it, namespace) ->
                    it.replace(namespace + "\\blockstates", DynamicCopperativeResourcePack.NAMESPACE + "\\blockstates")
            );
            cache.writeIfNeeded(Path.of(newPath), bytes, hash);
        });
    }
}
