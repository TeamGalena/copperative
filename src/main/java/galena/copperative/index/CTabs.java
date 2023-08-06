package galena.copperative.index;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class CTabs {

    private static final Collection<Entry<?>> ENTRIES = new ArrayList<>();

    public static <I extends ItemLike> void addToTab(Supplier<I> item, Supplier<I> after, ResourceKey<CreativeModeTab> tab) {
        ENTRIES.add(new Entry<>(item, after, tab));
    }

    public static <I extends ItemLike> void addToTab(Collection<Supplier<I>> items, @Nullable Supplier<I> after, ResourceKey<CreativeModeTab> tab) {
        items.stream().reduce(after, (previous, next) -> {
            addToTab(next, previous, tab);
            return next;
        });
    }

    public static void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        ENTRIES.forEach(it -> it.register(event));
    }

    public record Entry<I extends ItemLike>(Supplier<I> item, @Nullable Supplier<? extends ItemLike> after,
                                            ResourceKey<CreativeModeTab> tab) {

        private void register(BuildCreativeModeTabContentsEvent event) {
            if (!event.getTabKey().equals(tab)) return;

            var entries = event.getEntries();

            if (after != null) {
                var afterStack = new ItemStack(after.get());
                entries.putAfter(afterStack, new ItemStack(item.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            } else {
                event.accept(item);
            }
        }

    }

}
