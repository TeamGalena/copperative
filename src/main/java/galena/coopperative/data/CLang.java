package galena.coopperative.data;

import galena.coopperative.Coopperative;
import galena.coopperative.data.provider.CLangProvider;
import galena.coopperative.registry.CBlocks;
import galena.coopperative.registry.CItems;
import net.minecraft.data.DataGenerator;

public class CLang extends CLangProvider {

    public CLang(DataGenerator gen) {
        super(gen, Coopperative.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {



        /*
            Automatically create translations for blocks and items based on their registry name.

            This must be at the very bottom to avoid overwriting errors. These functions ignore objects
            that have already been translated above.
         */
        for (int i = 0; CBlocks.BLOCKS.getEntries().size() > i; i++) {
            tryBlock(CBlocks.BLOCKS.getEntries().stream().toList().get(i));
        }
        for (int i = 0; CItems.ITEMS.getEntries().size() > i; i++) {
            tryItem(CItems.ITEMS.getEntries().stream().toList().get(i));
        }
    }
}
