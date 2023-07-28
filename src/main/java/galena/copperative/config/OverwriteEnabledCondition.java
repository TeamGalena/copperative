package galena.copperative.config;

import com.google.gson.JsonObject;
import galena.copperative.Copperative;
import galena.copperative.index.CConversions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;

public class OverwriteEnabledCondition implements ICondition {

    private static final ResourceLocation ID = new ResourceLocation(Copperative.MOD_ID, "overwrite_enabled");

    private final ResourceLocation key;
    private final @Nullable CommonConfig.OverrideTarget target;

    public OverwriteEnabledCondition(ResourceLocation block, @Nullable CommonConfig.OverrideTarget target) {
        this.key = block;
        this.target = target;
    }

    public OverwriteEnabledCondition(Block output, @Nullable CommonConfig.OverrideTarget target) {
        this(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(CConversions.getFirst(output))), target);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        var block = ForgeRegistries.BLOCKS.getValue(key);
        if (block == null) return false;
        if (target == null) return CommonConfig.isOverwriteEnabled(block);
        return CommonConfig.isOverwriteEnabled(block, target);
    }

    public static class Serializer implements IConditionSerializer<OverwriteEnabledCondition> {

        @Override
        public void write(JsonObject json, OverwriteEnabledCondition value) {
            json.addProperty("block", value.key.toString());
            if (value.target != null) json.addProperty("target", value.target.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public OverwriteEnabledCondition read(JsonObject json) {
            var key = new ResourceLocation(GsonHelper.getAsString(json, "block"));
            var target = json.has("target")
                    ? CommonConfig.OverrideTarget.valueOf(GsonHelper.getAsString(json, "target").toUpperCase())
                    : null;
            return new OverwriteEnabledCondition(key, target);
        }

        @Override
        public ResourceLocation getID() {
            return OverwriteEnabledCondition.ID;
        }
    }

}
