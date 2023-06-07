package galena.coopperative.data.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import galena.coopperative.config.CommonConfig;
import galena.coopperative.index.CLootInjects;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class CopperNuggetConfigCondition implements LootItemCondition {

    @Override
    public LootItemConditionType getType() {
        return CLootInjects.CONFIG_LOOT_CONDITION.get();
    }

    @Override
    public boolean test(LootContext context) {
        return CommonConfig.injectCopperNuggets();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<CopperNuggetConfigCondition> {
        public void serialize(JsonObject json, CopperNuggetConfigCondition condition, JsonSerializationContext context) {
            // not needed
        }

        public CopperNuggetConfigCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return new CopperNuggetConfigCondition();
        }
    }

}
