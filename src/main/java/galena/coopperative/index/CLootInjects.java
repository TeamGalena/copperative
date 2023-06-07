package galena.coopperative.index;

import com.mojang.serialization.Codec;
import galena.coopperative.Coopperative;
import galena.coopperative.data.loot.AddLootEntry;
import galena.coopperative.data.loot.CopperNuggetConfigCondition;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CLootInjects {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Coopperative.MOD_ID);
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister.create(Registry.LOOT_ITEM_REGISTRY, Coopperative.MOD_ID);

    public static final RegistryObject<LootItemConditionType> CONFIG_LOOT_CONDITION = LOOT_CONDITIONS.register("inject_copper_nuggets", () -> new LootItemConditionType(new CopperNuggetConfigCondition.Serializer()));

    static {
        LOOT_MODIFIERS.register("inject_item", () -> AddLootEntry.CODEC);
    }

}
