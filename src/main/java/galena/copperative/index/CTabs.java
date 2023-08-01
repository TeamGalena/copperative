package galena.copperative.index;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class CTabs {


    private static final Collection<Pair<Supplier<? extends ItemLike>, ResourceKey<CreativeModeTab>>> ITEMS_TO_TABS = new ArrayList<>();

    public static void addToTab(Supplier<? extends ItemLike> supplier, ResourceKey<CreativeModeTab> tab) {
        ITEMS_TO_TABS.add(new Pair<>(supplier, tab));
    }

    public static void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        ITEMS_TO_TABS.stream()
                .filter(it -> it.getSecond().equals(event.getTabKey()))
                .forEach(it -> event.accept(it.getFirst()));
    }

}
