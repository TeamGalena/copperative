package galena.copperative.data;

import galena.copperative.data.provider.CLangProvider;
import galena.copperative.index.CBlocks;
import galena.copperative.index.CItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CLang extends CLangProvider {

    public CLang(DataGenerator gen) {
        super(gen, "en_us");
    }

    private String capitalize(String value) {
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1).toLowerCase(Locale.ROOT);
    }

    public void withWeatheredPrefix(Stream<? extends Supplier<Block>> blocks, String base) {
        blocks.forEach(block -> {
            if(block.get() instanceof WeatheringCopper copper && copper.getAge() != WeatheringCopper.WeatherState.UNAFFECTED) {
                var name = copper.getAge().name();
                var prefix = capitalize(name);
                addBlock(block, prefix + " " + base);
            } else {
                addBlock(block, base);
            }
        });
    }

    @Override
    protected void addTranslations() {
        addBlock(CBlocks.PATINA_BLOCK, "Block of Patina");
        withWeatheredPrefix(CBlocks.HEADLIGHT.stream(), "Redstone Headlight");
        withWeatheredPrefix(CBlocks.TOGGLER.stream(), "Redstone Toggler");

        withWeatheredPrefix(CBlocks.REPEATERS.weathered(), "Redstone Repeater");

        withWeatheredPrefix(CBlocks.COMPARATORS.weathered(), "Redstone Comparator");

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
