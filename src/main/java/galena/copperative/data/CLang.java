package galena.copperative.data;

import galena.copperative.data.provider.CLangProvider;
import galena.copperative.index.CBlocks;
import galena.copperative.index.CItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Locale;

public class CLang extends CLangProvider {

    public CLang(DataGenerator gen) {
        super(gen, "en_us");
    }

    public void withWeatheredPrefix(List<RegistryObject<Block>> blocks, String base) {
        blocks.forEach(block -> {
            if(block.get() instanceof WeatheringCopper copper && copper.getAge() != WeatheringCopper.WeatherState.UNAFFECTED) {
                var name = copper.getAge().name();
                var prefix = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(Locale.ROOT);
                addBlock(block, prefix + " " + base);
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

        addBlock(CBlocks.EXPOSED_REPEATER, "Exposed Redstone Repeater");
        addBlock(CBlocks.WEATHERED_REPEATER, "Weathered Redstone Repeater");
        addBlock(CBlocks.OXIDIZED_REPEATER, "Oxidized Redstone Repeater");

        addBlock(CBlocks.EXPOSED_COMPARATOR, "Exposed Redstone Comparator");
        addBlock(CBlocks.WEATHERED_COMPARATOR, "Weathered Redstone Comparator");
        addBlock(CBlocks.OXIDIZED_COMPARATOR, "Oxidized Redstone Comparator");

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
