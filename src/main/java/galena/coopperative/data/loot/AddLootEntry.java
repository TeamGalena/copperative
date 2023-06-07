package galena.coopperative.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AddLootEntry extends LootModifier {

    public static final Codec<AddLootEntry> CODEC = RecordCodecBuilder.create(builder ->
            codecStart(builder)
                    .and(Registry.ITEM.byNameCodec().fieldOf("item").forGetter(it -> it.item))
                    .and(Codec.INT.fieldOf("min").forGetter(it -> it.min))
                    .and(Codec.INT.fieldOf("max").forGetter(it -> it.max))
                    .apply(builder, AddLootEntry::new)
    );

    private final Item item;
    private final int min;
    private final int max;

    protected AddLootEntry(LootItemCondition[] conditions, Item item, int min, int max) {
        super(conditions);
        this.item = item;
        this.min = min;
        this.max = max;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var count = context.getRandom().nextIntBetweenInclusive(min, max);
        var stack = new ItemStack(item, count);
        if (!stack.isEmpty()) generatedLoot.add(stack);
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

}
