package galena.coopperative.content.index;

import galena.coopperative.Coopperative;
import galena.coopperative.content.item.PatinaItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Coopperative.MOD_ID);

    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item((new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS))));
    public static final RegistryObject<Item> PATINA = ITEMS.register("patina", () -> new PatinaItem((new Item.Properties()).tab(CreativeModeTab.TAB_MATERIALS)));
}
