package galena.copperative.index;

import galena.copperative.Copperative;
import galena.copperative.content.item.PatinaItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Copperative.MOD_ID);

    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item((new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS))));
    public static final RegistryObject<Item> PATINA = ITEMS.register("patina", () -> new PatinaItem((new Item.Properties()).tab(CreativeModeTab.TAB_MATERIALS)));

    public static final TagKey<Item> WAX_INDICATORS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Copperative.MOD_ID, "wax_indicators"));

}
