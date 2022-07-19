package galena.coopperative.data;

import galena.coopperative.Coopperative;
import galena.coopperative.data.provider.CItemModelProvider;
import galena.coopperative.registry.CBlocks;
import galena.coopperative.registry.CItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CItemModels extends CItemModelProvider {

    public CItemModels(DataGenerator gen, ExistingFileHelper help) {
        super(gen, help);
    }

    @Override
    public String getName() {
        return Coopperative.MOD_ID + " Item Models";
    }

    @Override
    protected void registerModels() {
        normalItem(CItems.COPPER_NUGGET);
        normalItem(CItems.PATINA);

        block(CBlocks.OBSERVER);
        block(CBlocks.EXPOSED_OBSERVER);
        block(CBlocks.WEATHERED_OBSERVER);
        block(CBlocks.OXIDIZED_OBSERVER);
    }
}
