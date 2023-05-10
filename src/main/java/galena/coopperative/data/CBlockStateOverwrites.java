package galena.coopperative.data;

import galena.coopperative.client.DynamicCooperativeResourcePack;
import galena.coopperative.data.provider.CBlockStateProvider;
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
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        super.run((path, bytes, hash) -> {
            var newPath = Path.of(path.toString().replace("minecraft\\blockstates", DynamicCooperativeResourcePack.NAMESPACE + "\\blockstates"));
            cache.writeIfNeeded(newPath, bytes, hash);
        });
    }
}
