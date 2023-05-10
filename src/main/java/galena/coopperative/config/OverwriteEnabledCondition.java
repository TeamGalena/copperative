package galena.coopperative.config;

import com.google.gson.JsonObject;
import galena.coopperative.Coopperative;
import galena.coopperative.index.CConversions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class OverwriteEnabledCondition implements ICondition {

    private static final ResourceLocation ID = new ResourceLocation(Coopperative.MOD_ID, "overwrite_enabled");

    private final ResourceLocation key;

    public OverwriteEnabledCondition(ResourceLocation block) {
        this.key = block;
    }

    public OverwriteEnabledCondition(Block output) {
        this(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(CConversions.getFirst(output))));
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        var block = ForgeRegistries.BLOCKS.getValue(key);
        return block != null && CommonConfig.isOverwriteEnabled(block);
    }

    public static class Serializer implements IConditionSerializer<OverwriteEnabledCondition> {

        @Override
        public void write(JsonObject json, OverwriteEnabledCondition value) {
            json.addProperty("block", value.key.toString());
        }

        @Override
        public OverwriteEnabledCondition read(JsonObject json) {
            var key = new ResourceLocation(GsonHelper.getAsString(json, "block"));
            return new OverwriteEnabledCondition(key);
        }

        @Override
        public ResourceLocation getID() {
            return OverwriteEnabledCondition.ID;
        }
    }

}
