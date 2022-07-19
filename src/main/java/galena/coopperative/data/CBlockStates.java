package galena.coopperative.data;

import galena.coopperative.data.provider.CBlockStateProvider;
import galena.coopperative.registry.CBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CBlockStates extends CBlockStateProvider {

    public CBlockStates(DataGenerator gen, ExistingFileHelper help) {
        super(gen, help);
    }

    @Override
    public String getName() {
        return "Cooperative Block States";
    }

    @Override
    protected void registerStatesAndModels() {
        observer(CBlocks.OBSERVER);
        observer(CBlocks.EXPOSED_OBSERVER);
        observer(CBlocks.WEATHERED_OBSERVER);
        observer(CBlocks.OXIDIZED_OBSERVER);
    }
}
