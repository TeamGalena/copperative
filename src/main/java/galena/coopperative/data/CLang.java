package galena.coopperative.data;

import galena.coopperative.data.provider.CLangProvider;
import galena.coopperative.index.CBlocks;
import galena.coopperative.index.CItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class CLang extends CLangProvider {

    public CLang(DataGenerator gen) {
        super(gen, "en_us");
    }

    public void withWeatheredPrefix(List<RegistryObject<Block>> blocks, String base) {
        blocks.forEach(block -> {
            if(block.get() instanceof WeatheringCopper copper) {
                addBlock(block, formatString(copper.getAge().name() + " " + base));
            } else {
                addBlock(block, base);
            }
        });
    }

    @Override
    protected void addTranslations() {

        addBlock(CBlocks.PATINA_BLOCK, "Block of Patina");
        withWeatheredPrefix(CBlocks.HEADLIGHT, "Redstone Headlight");
        withWeatheredPrefix(CBlocks.TOGGLER, "Redstone Toggler");

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
