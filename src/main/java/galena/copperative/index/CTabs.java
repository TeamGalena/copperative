package galena.copperative.index;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public class CTabs {


    private static final HashMap<Supplier<? extends ItemLike>, ResourceKey<CreativeModeTab>> ITEMS_TO_TABS = new HashMap<>();

    public static void addToTab(Supplier<? extends ItemLike> supplier, ResourceKey<CreativeModeTab> tab) {
        ITEMS_TO_TABS.put(supplier, tab);
    }

    public static void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        ITEMS_TO_TABS.entrySet().stream()
                .filter(it -> it.getValue().equals(event.getTabKey()))
                .forEach(it -> event.accept(it.getKey()));
    }

}
